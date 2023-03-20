var mongoose = require("mongoose");

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect("mongodb://127.0.0.1/myDatabase");

var Schema = mongoose.Schema;

var commentSchema = new Schema({
  // an _id will be automatically created
  author: {type: String},
  text: {type: String}
}, {timestamps: true});

var courseSchema = new Schema({
  name: { type: String, required: true },
  number: { type: String, required: true, unique: true },
  instructor: String,
  department: String,
  school: String,
  description: String,
  rating: Number,
  comments: [commentSchema] // an array holding all comments
});

// export courseSchema as a class called Course
module.exports = mongoose.model("Course", courseSchema);
