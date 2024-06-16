#import libraries
import numpy as np
import base64
import re
import cv2
from io import BytesIO
import base64
import io
import time
from collections import OrderedDict
import json

# import matplotlib for plotting
from matplotlib.pyplot import imshow
import matplotlib.pyplot as plt

# import Flask
from flask import Flask
from flask import render_template, request

# import image processing
import sys
sys.path.insert(0, '../')

# import pytorch
import tensorflow as tf

# Dictionary with label codes
arabic_chars = {0: 'alif',
                1: 'ba',
                2: 'ta',
                3: 'tsa',
                4: 'jim',
                5: 'kha',
                6: 'kho',
                7: 'dal',
                8: 'dzal',
                9: 'ra',
                10: 'zai',
                11: 'sin',
                12: 'syin',
                13: 'shad',
                14: 'dhad',
                15: 'tha',
                16: 'dha',
                17: 'ain',
                18: 'ghain',
                19: 'fa',
                20: 'qaf',
                21: 'kaf',
                22: 'lam',
                23: 'mam',
                24: 'nun',
                25: 'Ha',
                26: 'waw',
                27: 'ya'
               }

def load_model(filepath = "/home/ai/Development/alifLam/notebook/100624_arabic_model.h5"):
    """
    Function loads the model from checkpoint.

    INPUT:
        filepath - path for the saved model

    OUTPUT:
        model - loaded pytorch model
    """

    print("Loading model from {} \n".format(filepath))

    model = tf.keras.models.load_model(filepath)

    return model


app = Flask(__name__)

# load model
model = load_model()

# index webpage receives user input for the model
@app.route('/')
@app.route('/index')
def index():
    # render web page
    return render_template('index.html')

@app.route('/go/<dataURL>')
def pred(dataURL):
    """
    Render prediction result.
    """

    # decode base64  '._-' -> '+/='
    dataURL = dataURL.replace('.', '+')
    dataURL = dataURL.replace('_', '/')
    dataURL = dataURL.replace('-', '=')

    # get the base64 string
    image_b64_str = dataURL
    # convert string to bytes
    byte_data = base64.b64decode(image_b64_str)
    # open Image with PIL
    img = cv2.imdecode(np.frombuffer(byte_data, np.uint8), -1)
    trans_mask = img[:,:,3] == 0
    img[trans_mask] = [255, 255, 255, 255]
    # set white background
    img = cv2.cvtColor(img, cv2.COLOR_RGBA2GRAY)
    img = cv2.bitwise_not(img)  # Invert colors
    cv2.imwrite('image1.png', img)
    # more beautiful resize
    img = cv2.resize(img, (32, 32), interpolation=cv2.INTER_AREA)
    # threshold
    _, img = cv2.threshold(img, 50, 255, cv2.THRESH_BINARY)
    # blur again
    img = cv2.GaussianBlur(img, (3, 3), 0)
    cv2.imwrite('image2.png', img)

    img = img.reshape(-1, 32, 32, 1)
    img = img.astype('float32') / 255.0

    # apply model and print prediction
    pred = model.predict(img)
    predicted_label = arabic_chars[np.argmax(pred)]
    print("This is a {}".format(predicted_label))

    # return in h1
    return "<h1>This is a {}</h1>".format(predicted_label)

def main():
    app.run(host='0.0.0.0', port=3001, debug=True)


if __name__ == '__main__':
    main()