const { Firestore } = require('@google-cloud/firestore');
const { processImage } = require('./inference');

const firestore = new Firestore();

async function handleImageUpload(req, h) {
    try {
        const base64Image = req.payload.image;
        const imageBuffer = Buffer.from(base64Image, 'base64');

        // Log the incoming request for debugging
        console.log("Received image upload request");

        // Process the image using TensorFlow.js
        const { extractedText, accuracy, message } = await processImage(imageBuffer);

        // Store results in Firestore
        const docRef = firestore.collection('images').doc();
        await docRef.set({
            extractedText,
            accuracy,
            message,
            timestamp: Firestore.FieldValue.serverTimestamp()
        });

        // Log the response for debugging
        console.log("Image processed and stored successfully");

        // Return the results
        return h.response({
            extractedText,
            accuracy,
            message
        }).code(200);
    } catch (error) {
        console.error("Error handling image upload:", error);
        return h.response(error.toString()).code(500);
    }
}

module.exports = { handleImageUpload };