# Cloud Computing (CC) Branch

This branch is for the cloud computing part of our capstone project. The code in this branch implements a REST API to support the backend of the AlifLam mobile application features. We also use Google Cloud Platform to use the cloud services.

## Installation

1. Enable 3 Google Cloud API : Artifact Registry, Cloud Build, Cloud Run.
   
       gcloud services enable artifactregistry.googleapis.com cloudbuild.googleapis.com run.googleapis.com
   
3. Clone this repository to your Cloud CLI using

       https://github.com/C241-PS493-AlifLam/aliflam-be.git
   
5. Navigate to the cloned repository directory.

        cd [directory path]
   
7. Make an Artifact Repository from the cloned repository 
   
       gcloud artifacts repositories create <repository name> --repository-format=docker --location=asia-southeast2 --async
   
8. Build an Image from that Artifact Registry 

        gcloud builds submit --tag asia-southeast2-docker.pkg.dev/${GOOGLE_CLOUD_PROJECT}/<repository name>/<image name>:<version>
   
9. Deploy the service by running, and wait for the deployment process to complete.

        gcloud run deploy --image asia-southeast2-docker.pkg.dev/${GOOGLE_CLOUD_PROJECT}/<repository name>/<image name>:<version>
   
11. Once deployed, the service is ready to be used!

## Features

There are several routes in the backend of this API service based on their features. Here is the route list:

1. **Predict Words in Canvas (ML Feature)** : `POST /upload` , This API endpoint is use to run ML model to predict the Arabic character based on the image drawn on the canvas.


For more information, you can visit our **Postman API Documentation** on : [API Documentation](https://documenter.getpostman.com/view/34809133/2sA3XV8KTs).

## Contact Information

If you have any questions, feedback, or suggestions related to the cloud computing branch, feel free to reach out to us at :

- Aby Nurangga c006d4ky0196@bangkit.academy
- Nadhirul Fatah Ulhaq c006d4ky1331@bangkit.academy 

## Acknowledgements

We also extend our heartfelt gratitude to the following individuals for their invaluable contributions to the success of our cloud computing project

- Aby Nurangga (Core CC Team)
- Nadhirul Fatah Ulhaq (Core CC Team)
- Capstone Project Group C241-PS493
- Bangkit Mentor
