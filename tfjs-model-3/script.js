async function loadModel() {
    const model = await tf.loadLayersModel('tfjs-model-3/model.json');
    return model;
}

function preprocessImage(image) {
    let tensor = tf.browser.fromPixels(image)
        .resizeNearestNeighbor([32, 32])
        .mean(2)
        .toFloat()
        .expandDims(0)
        .expandDims(-1)
        .div(255.0);
    return tensor;
}

async function predict(image) {
    const model = await loadModel();
    const tensor = preprocessImage(image);
    const prediction = model.predict(tensor);
    const labelIndex = prediction.argMax(-1).dataSync()[0];
    const labels = ['alif', 'ba', 'ta', 'tsa', 'jim', 'hâ', 'khâ', 'dal', 'dzal', 'ra', 'zai', 'sîn', 'syîn', 'shâd', 'dhâd', 'thâ', 'zhâ', 'âin', 'ghain', 'fa', 'qâf', 'kâf', 'lam', 'mîm', 'nun', 'Ha', 'waw', 'yâ'];
    document.getElementById('prediction').innerText = `Predicted: ${labels[labelIndex]}`;
}

document.getElementById('file-input').addEventListener('change', (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = (e) => {
        const img = new Image();
        img.src = e.target.result;
        img.onload = () => predict(img);
    };
    reader.readAsDataURL(file);
});
