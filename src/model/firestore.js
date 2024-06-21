const firestore = require('@google-cloud/firestore');

const db = new firestore({
  // databaseId : "credential"
});

//Register
const createUser = async (username, hashedPassword) => {
  const userRef = db.collection('users').doc(username);
  await userRef.set({
    username,
    password: hashedPassword
  });
};

//Cek User
const findUser = async (username) => {
  const userRef = db.collection('users').doc(username);
  const userSnapshot = await userRef.get();
  return userSnapshot.exists ? userSnapshot.data() : null;
};

module.exports = {
  createUser,
  findUser
};
