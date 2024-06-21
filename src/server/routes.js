const { handleImageUpload } = require('../ml/handler-ml');
const { registerHandler, loginHandler, profileHandler } = require('./handler');

const routes = [
  {
    method: 'POST',
    path: '/register',
    handler: registerHandler
  },
  {
    method: 'POST',
    path: '/login',
    handler: loginHandler
  },
  {
    method: 'GET',
    path: '/secure/profile',
    handler: profileHandler,
  },
  {
    method: 'POST',
    path: '/upload',
    handler: handleImageUpload,
    options: {
        payload: {
            allow: "multipart/form-data",
            // output: 'stream',
            // parse: true,
            multipart: true,
            maxBytes: 10485760 // 10 MB
        },
    }
  },
  // {
  //   method: 'POST',
  //   path: '/other-prediction',
  //   options: {
  //       payload: {
  //           output: 'stream',
  //           parse: true,
  //           multipart: true,
  //           maxBytes: 10485760 // 10 MB
  //       },
  //       handler: (req, h) => handleOtherPrediction(req, h)
  //   }
  // }
];

module.exports = routes;
