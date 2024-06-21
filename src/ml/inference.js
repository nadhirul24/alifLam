const tf = require('@tensorflow/tfjs-node');
const fetch = require('node-fetch');

async function loadModel() {
    try {
        const modelUrl = 'https://storage.googleapis.com/try-models-bucket-aliflam/300524_arabic_model.json';
        const response = await fetch(modelUrl, { timeout: 60000 }); // Increase timeout to 60 seconds
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const model = await tf.loadLayersModel(modelUrl);
        console.log("Model loaded successfully");
        return model;
    } catch (error) {
        console.error("Error loading the model:", error);
        throw new Error("Failed to load model: " + error.message);
    }
}

async function processImage(imageBuffer) {
    try {
        // Decode the image buffer
        let imageTensor = tf.node.decodeImage(imageBuffer);

        // Resize the image to 32x32 pixels
        imageTensor = tf.image.resizeBilinear(imageTensor, [32, 32]);

        // Convert the image to grayscale if it has 3 or 4 channels (RGB or RGBA)
        if (imageTensor.shape[2] === 3 || imageTensor.shape[2] === 4) {
            imageTensor = imageTensor.mean(2).expandDims(2);
        }

        // Normalize the image to have values between 0 and 1
        imageTensor = imageTensor.div(255.0);

        // Expand the dimensions to match the expected input shape [1, 32, 32, 1]
        const inputTensor = imageTensor.expandDims(0);

        // Load the model and make a prediction
        const model = await loadModel();
        const prediction = model.predict(inputTensor);

        // Decode the prediction and calculate accuracy
        const extractedText = decodePrediction(prediction);
        const accuracy = computeAccuracy(prediction);

        // Prepare the response message based on accuracy
        let message = '';
        if (accuracy > 0.9) {
            message = 'The image is true';
        } else {
            message = 'The image accuracy is less than 90%';
        }

        return { extractedText, accuracy, message };
    } catch (error) {
        console.error("Error processing the image:", error);
        throw new Error("Failed to process image: " + error.message);
    }
}

function decodePrediction(prediction) {
    const arabic_chars = [
        'alif', 'ba', 'ta', 'tsa', 'jim', 'hâ', 'khâ', 'dal', 'dzal', 'ra',
        'zai', 'sîn', 'syîn', 'shâd', 'dhâd', 'thâ', 'zhâ', 'âin', 'ghain', 'fa',
        'qâf', 'kâf', 'lam', 'mîm', 'nun', 'Ha', 'waw', 'yâ'
    ];

    const predictedIndex = prediction.argMax(1).dataSync()[0];
    const predictedCharacter = arabic_chars[predictedIndex];

    return predictedCharacter;
}

function computeAccuracy(prediction) {
    const accuracy = prediction.max().dataSync()[0];
    return accuracy;
}

module.exports = { processImage };