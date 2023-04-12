var mongoose = require("mongoose");

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect("mongodb://127.0.0.1/myDatabase");

var Schema = mongoose.Schema;
const { commentSchema } = require("./Course");

var userSchema = new mongoose.Schema({
  email: { type: String, required: true, unique: true, index: true },
  name: { type: String },
  password: { type: String },
});

userSchema.set("primaryKey", "email");

// export userSchema as a class called User
module.exports = mongoose.model("User", userSchema);
