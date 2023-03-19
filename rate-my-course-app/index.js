var express = require("express");
var app = express();

// set up BodyParser
var bodyParser = require("body-parser");
app.use(bodyParser.urlencoded({ extended: true }));

// import the Course class from Course.js
var Course = require("./Course.js");

/***************************************/

// endpoint for adding a new course
// this is the action of the "add new course" form
app.use("/add", async (req, res) => {
  // construct the Course from the form data which is in the request body
  var newCourse = new Course({
    name: req.body.name,
    number: req.body.number,
    instructor: req.body.instructor,
    department: req.body.department,
    school: req.body.school,
    description: req.body.description,
    rating: req.body.rating,
  });

  // save the course to the database
  try {
    const result = await newCourse.save();
    res.send("successfully added " + newCourse.name + " to the database");
  } catch (err) {
    res.type("html").status(200);
    res.write("uh oh: " + err);
    console.log(err);
    res.end();
  }
  //   newCourse.save((err) => {
  //     if (err) {
  //       res.type("html").status(200);
  //       res.write("uh oh: " + err);
  //       console.log(err);
  //       res.end();
  //     } else {
  //       // display the "successfull added" message
  //       res.send("successfully added " + newCourse.name + " to the database");
  //     }
  //   });
});

// User Story 1, the following are just examples from Lab 5, need to be re-implemented
// endpoint for showing all the courses
// app.use("/all", (req, res) => {
//   // find all the Course objects in the database
//   Course.find({}, (err, courses) => {
//     if (err) {
//       res.type("html").status(200);
//       console.log("uh oh" + err);
//       res.write(err);
//     } else {
//       if (courses.length == 0) {
//         res.type("html").status(200);
//         res.write("There are no courses");
//         res.end();
//         return;
//       } else {
//         res.type("html").status(200);
//         res.write("Here are the courses in the database:");
//         res.write("<ul>");
//         // show all the courses
//         courses.forEach((course) => {
//           res.write("<li>");
//           res.write(
//             "Course Name: " + course.name + "; Course Number: " + course.number
//           );
//           // this creates a link to the /delete endpoint
//           res.write(' <a href="/delete?name=' + course.name + '">[Delete]</a>');
//           res.write("</li>");
//         });
//         res.write("</ul>");
//         res.end();
//       }
//     }
//   }).sort({ number: "asc" }); // this sorts them BEFORE rendering the results
// });

// User Story 4
// delete a course
app.use("/delete", async (req, res) => {
  var filter = { number: req.body.number };
  try {
    const result = await Course.findOneAndDelete(filter);
    console.log(result);
  } catch (err) {
    console.log("error");
  }
  // Course.findOneAndDelete(filter, (err, course) => {
  //   if (err) {
  //     console.log("error");
  //   } else if (!course) {
  //     console.log("no course");
  //   } else {
  //     console.log("success");
  //   }
  // });
  res.redirect("/all");
});

// Example from Lab 5, need to be re-implemented
// endpoint for accessing data via the web api
// to use this, make a request for /api to get an array of all Course objects
// or /api?name=[whatever] to get a single object
// app.use("/api", (req, res) => {
//   // construct the query object
//   var queryObject = {};
//   if (req.query.name) {
//     // if there's a name in the query parameter, use it here
//     queryObject = { name: req.query.name };
//   }

//   Course.find(queryObject, (err, courses) => {
//     console.log(courses);
//     if (err) {
//       console.log("uh oh" + err);
//       res.json({});
//     } else if (courses.length == 0) {
//       // no objects found, so send back empty json
//       res.json({});
//     } else if (courses.length == 1) {
//       var course = courses[0];
//       // send back a single JSON object
//       res.json({ name: course.name, number: course.number });
//     } else {
//       // construct an array out of the result
//       var returnArray = [];
//       courses.forEach((course) => {
//         returnArray.push({ name: course.name, number: course.number });
//       });
//       // send it back as JSON Array
//       res.json(returnArray);
//     }
//   });
// });

/*************************************************/

app.use("/templates", express.static("templates"));

app.use("/", (req, res) => {
  res.redirect("/templates/homepage.html");
});

app.listen(3000, () => {
  console.log("Listening on port 3000");
});
