// const bcrypt = require('bcrypt');
const firestore = require('@google-cloud/firestore');

const db = new firestore({
  // databaseId : "credential"
});
const jwt = require('jsonwebtoken');
const { createUser, findUser } = require('../model/firestore');

const JWT_SECRET = 'your_jwt_secret_key'; // Ganti dengan secret key Anda

const generateToken = (username) => {
  return jwt.sign({ username }, JWT_SECRET, { expiresIn: '12h' });
};

//Register
const registerHandler = async (request, h) => {
  const { username, password } = request.payload;

  //encryp password
  // const hashedPassword = await bcrypt.hash(password, 10);

  const existingUser = await findUser(username);
  if (existingUser) {
    return h.response({ error: 'Username already exists' }).code(400);
  }

  await createUser(username, password);

  return h.response({ message: 'User registered successfully' }).code(201);
};

//Login
const loginHandler = async (request, h) => {
  const { username, password } = request.payload;

  const user = await findUser(username);
  if (!user) {
    return h.response({ error: 'Invalid username or password' }).code(400);
  }

  const isValidPassword = password === user.password;
  if (!isValidPassword) {
    return h.response({ error: 'Invalid username or password' }).code(400);
  }

  const token = generateToken(username);
  const userRef = db.collection('users').doc(username).add(token);

  return h.response({ message: 'Login successful', token }).code(200);
};

const profileHandler = async (request, h) => {
  return h.response({ message: `Welcome ${request.auth.username}` }).code(200);
};

module.exports = {
  registerHandler,
  loginHandler,
  profileHandler
};
