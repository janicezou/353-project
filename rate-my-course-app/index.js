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
    res.type("html").status(200);
    res.write("sucessfully added " + newCourse.name + " to the database");
    res.write(' <a href="/templates/homepage.html">[HOME]</a>');
    res.end();
    return;
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

/**
 * User Story 1 & 2
 * Author: Quinn
 * Date modified: Mar 26, 2022
 * view all courses
 * navigate to a course
 */
// app.get("/viewCourse/:coursenumber") // display one course info
app.use("/all", (req, res) => {
  Course.find({}, (err, allCourses) => {
    if (err) {
      res.type("html").status(200);
      res.write("An error occured. Could not find any course.")
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end()
      return;
    } else if (allCourses.length == 0) {
      res.type("html").status(200);
      res.write("There are no courses.")
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end()
      return;
    } else {
      res.type("html").status(200);
      res.write("There are " + allCourses.length + " courses in the database.")
      res.write("<ul>");
      allCourses.forEach((course) => {
        res.write("<li>");
        if(course){
          res.write('<a href="/viewCourse/' + course.number + '">'+ course.name + '</a><br>')
          res.write('instructor: ' + course.instructor + ' department: ' + course.department + ' school: ' + course.school)
          res.write('<br> <a href="/addComment/' + course.number + '">Add a Comment (Test purpose only)</a><br>')
        }
        res.write("</li>");
      })
      res.write("</li>");
      res.end()
    }
  }).sort({ number: "asc" })
})

app.get("/viewCourse/:number", (req, res) => {
  var courseNum = req.params.number
  var queryObject = { number: courseNum }
  Course.find(queryObject, (err, course) => {
    if (err) {
      res.type('html').status(200)
      res.write("An error occured.")
      res.end()
    } else if (course.length == 0) {
      res.type('html').status(200)
      res.write("Course not found")
      res.end()
    } else {
      res.type('html').status(200)
      singleCourse = course[0]
      res.write(singleCourse.number + " " + singleCourse.name + "<br> instructor: " + singleCourse.instructor)
      res.write("<br> department: " + singleCourse.department + ' <br> school: ' + singleCourse.school)
      res.write('<br> description: ' + singleCourse.description)
      if(singleCourse.rating){
        res.write('<br> rating: ' + singleCourse.rating)
      }
      res.write('<br> <a href="/viewComments/' + singleCourse.number + '">View Comments</a><br>')
      res.write('<br> <a href="/addComment/' + singleCourse.number + '">Add a Comment (Test purpose only)</a><br>')
      res.write('<a href="/edit/' + singleCourse.number + '">[EDIT]</a><br>')
      res.write('<a href="/templates/homepage.html\">[HOME]</a><br>')
      res.end()
    }
  })
})

// User Story 4
// delete a course
app.use("/delete", async (req, res) => {
  // res.redirect(' <a href="/internalDelete?number=' + req.body.number + '">[Delete]</a>');
  var filter = { number: req.body.number };
  try {
    const result = await Course.findOneAndDelete(filter);
    console.log(result);
    if(result){
      res.type("html").status(200);
      res.write("Sucessfully deleted "+req.body.number); 
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
    } else {
      res.type("html").status(200);
      res.write("No such course in database"); 
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
    }
    res.end();
    return;
  } catch (err) {
    console.log("uh oh" + err);
    res.end();
    return;
  }
});

/**
 * User Story 3
 * Author: Xinran
 * Date modified: Mar 19, 2022
 * edit information about a specific course
 * @Param number: the course number for the course (primary key)
 */
app.get("/edit/:number", (req, res) => {
  var courseNum = req.params.number;
  var filter = { number: courseNum };
  Course.find(filter, (err, courses) => {
    if (err) {
      res.type("html").status(200);
      res.write(err);
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else if (courses.length == 0) {
      res.type("html").status(200);
      res.write("Course not found");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else {
      var course = courses[0]
      res.type("html").status(200);
      res.write('Edit information for '+ course.number)
      res.write('<form id="edit-form" action = "/edit/' + courseNum + '" method="post">')
      res.write('<label for="name">Name:</label>')
      res.write('<input type="text" id="name" name="name" value="'+course.name+'"><br>')
      res.write('<label for="instructor">Instructor Name:</label>')
      res.write('<input type="text" id="instructor" name="instructor" value="'+course.instructor+'"><br>')
      res.write('<label for="department">Course Department:</label>')
      res.write('<input type="text" id="department" name="department" value="'+course.department+'"><br>')
      res.write('<label for="school">Course school:</label>')
      res.write('<input type="text" id="school" name="school" value="'+course.school+'"><br>')
      res.write('<label for="description">Course Description:</label>')
      res.write('<input type="text" id="description" name="description" value="'+course.description+'"><br>')
      res.write('<input type="submit" value="Save Changes">')
      res.write('</form><br>')
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
      res.write(' <a href="/internalDelete?number=' + courseNum + '">[Delete]</a>');
      res.end();
    }
  });
});

app.post("/edit/:number", (req, res) => {
  var courseNum = req.params.number;
  var filter = { number: courseNum };
  var courseName = req.body.name;
  var instructor = req.body.instructor;
  var department = req.body.department;
  var school = req.body.school;
  var description = req.body.description;

  var action = {};
  var actionObj = {}
  if (courseName) {
    actionObj.name = courseName
  }
  if (instructor) {
    actionObj.instructor = instructor;
  }
  if (department) {
    actionObj.department = department;
  }
  if (school) {
    actionObj.school = school;
  }
  if (description) {
    actionObj.description = description;
  }

  action = {'$set': actionObj}

  Course.findOneAndUpdate(filter, action, (e, p) => {
    if (e) {
      res.type('html').status(200)
      res.write("Unsucessfully edit course information: " + e +"<br>");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else if (!p) {
      res.type('html').status(200)
      res.write("Course not found <br>");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else {
      res.type('html').status(200)
      res.write("Successfully updated the course" + p.name + "to the database <br>");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    }
  });
});

/**
 * User Story 3
 * Author: Xinran
 * Date modified: Mar 19, 2022
 * view the comments made about a specified course
 * @Param number: the course number for the course (primary key)
 */
app.get("/viewComments/:number", (req, res) => {
  var courseNum = req.params.number;
  var queryObject = { number: courseNum };
  Course.find(queryObject, (err, courses) => {
    console.log(courses);
    if (err) {
      console.log("error" + err);
      res.type('html').status(200)
      res.write("Unsucessfully added comment to the database: " + err);
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else if (courses.length == 0) {
      res.type('html').status(200)
      res.write("Course not found");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else {
      // since number is primary key, it is not possible to have more than one course having the same name
      var singleCourse = courses[0];
      var comments = singleCourse.comments;
      res.type('html').status(200);
			res.write('Here are all comments for ' + singleCourse.name + ' in the database:');
			res.write('<ul>');
			// show all the comments
			comments.forEach( (comment) => {
        res.write("<li>")
        res.write('ID: ' + comment._id);
        res.write('<ul>');
        res.write('<li>');
        res.write('Rating: ' + comment.rating);
        res.write('</li>'); 
        res.write('<li>');
        res.write('Comment: ' + comment.text);
        res.write('</li>'); 
        res.write('<li>');
        res.write(" <a href=\"/deleteComment/" + courseNum + "/" + comment._id + "\">[DELETE]</a>");
        res.write(" <a href=\"/editComment/" + courseNum + "/" + comment._id + "\">[EDIT]</a>");
        res.write('<li>');
        res.write('</ul>');
        res.write('</li>')
			});
			res.write('</ul>');
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    }
  });
});

/**
 * Add a comment (for test purpose only)
 * Author: Xinran
 * Date Modified: Mar. 20, 2022
 * @param number: course number
 */

app.get("/addComment/:number", (req, res) => {
  var courseNum = req.params.number;
  var queryObject = { number: courseNum };
  Course.find(queryObject, (e, courses) =>{
    if(e){
      res.type("html").status(200);
      res.write("Error " + e);
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else if (courses.length == 0) {
      res.type("html").status(200);
      res.write("Course not found");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else {
      var course = courses[0]
      res.type("html").status(200);
      res.write('Add comment for '+ course.name)
      res.write('<form id="add-comment-form" action = "/addComment/' + courseNum + '" method="post">')
      res.write('<label for="rating">Rating:</label>')
      res.write('<input type="text" id="rating" name="rating" value="">')
      res.write('<label for="text">Comment:</label>')
      res.write('<input type="text" id="text" name="text" value="">')
      res.write('<input type="submit" value="Submit comment">')
      res.write('</form>')
      res.end();
    }
  })
});

app.post("/addComment/:number", (req, res) => {
  var courseNum = req.params.number;
  var queryObject = { number: courseNum };
  var comment = req.body.text;
  var rating = Number(req.body.rating);
  console.log(req.body.text)
  var commentObj = { author: "rand", rating: rating, text: comment };
  var action = { '$push': { comments: commentObj } };
  Course.findOneAndUpdate(queryObject, action, (err, course) => {
    if (err) {
      res.type("html").status(200);
      console.log("failed in adding comments")
      res.write("Failed in adding comment for "+ courseNum + ": " + err); 
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
      res.end()
    } else {
      res.type("html").status(200);
      console.log("succeeded in adding comments")
      res.write("Sucessfully added comment for "+ courseNum); 
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
      res.end()
    }
  });
});

/**
 * User story 5
 * Author: Ary
 * Date modified: Mar 24. 2023
 * I can perform a course object search by name/number/department/professor 
 * @Param name: the course name 
 * @Param number: the course number for the course (primary key)
 * @Param department: the department id 
 * @Param professor: the course professor
 */
app.use("/search", async (req, res) => {
  var filter = {};
  if(req.body.name){
    filter.name = req.body.name;
  }
  if(req.body.number){
    filter.number = req.body.number;
  }
  if(req.body.department){
    filter.department = req.body.department;
  }
  if(req.body.professor){
    filter.instructor = req.body.professor;
  }
  // res.send(filter);
  // find filtered Course objects in the database
  try {
    const result = await Course.find(filter).sort({ rating: "asc" });
    console.log(result);
    if (result.length == 0) {
      res.type("html").status(200);
      res.write("There are no courses matching the search criteria");
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
      res.end();
      return;
    } else {
      res.type("html").status(200);
      res.write("Here are the courses in the database:");
      res.write("<ul>");
      // show all the courses
      result.forEach((course) => {
        res.write("<li>");
        res.write( "Course Name: " + course.name + "; Course Number: " + course.number);
        res.write(' <a href="/internalDelete?number=' + course.number + '">[Delete]</a>');
      })
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
      res.end();
      return;

    }
  } catch (err) {
      res.type("html").status(200);
      console.log("uh oh" + err);
      res.write(err);
      res.end();
      return;
  }
  
});

app.use("/searching", async (req, res) => {
  var filter = {};
  if(req.query.name){
    filter.name = req.query.name;
  }
  if(req.query.number){
    filter.number = req.query.number;
  }
  if(req.query.department){
    filter.department = req.query.department;
  }
  if(req.query.professor){
    filter.instructor = req.query.professor;
  }
  // res.send(filter);
  // find filtered Course objects in the database
  try {
    const result = await Course.find(filter).sort({ rating: "asc" });
    console.log(result);
    res.type("html").status(200);
    res.json(result);
    return;
  } catch (err) {
      res.type("html").status(200);
      console.log("uh oh" + err);
      res.write(err);
      res.end();
      return;
  }
  
});

app.use("/internalDelete", async (req, res) => {
  var filter = { number: req.query.number };
  try {
    const result = await Course.findOneAndDelete(filter);
    console.log(result);
    if(result){
      res.type("html").status(200);
      res.write("Sucessfully deleted "+ req.query.number); 
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
    } else {
      res.type("html").status(200);
      res.write("No such course in database"); 
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>');
    }
    res.end();
    return;
  } catch (err) {
    console.log("uh oh" + err);
    res.end();
    return;
  }
});


/**
 * User story 6
 * Author: Xinran
 * Date modified: Mar 19. 2022
 * Generate the form for edit information
 * @Param number: the course number for the course (primary key)
 * @Param _id: the id for the comment to be deleted or edited
 */
app.get("/editComment/:number/:comment_id", (req, res) => {
  var courseNum = req.params.number;
  var comment_id = req.params.comment_id;
  var queryObject = { number: courseNum};
  Course.find(queryObject, (err, courses) => {
    console.log(courses)
    if (err) {
      res.type("html").status(200);
      res.write(err);
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else if (courses.length == 0) {
      res.type("html").status(200);
      res.write("Course not found");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else {
      var course = courses[0]
      res.type("html").status(200);
      res.write('Edit comment' + comment_id +'for '+ course.name)
      res.write('<form id="edit-comment-form" action ="/editComment/'+courseNum+'/'+ comment_id+'" method="post">')
      res.write('<label for="rating">Rating:</label>')
      res.write('<input type="text" id="rating" name="rating" value="'+course.comments.id(comment_id).rating+'">' +'<br>')
      res.write('<label for="text">Comment:</label>')
      res.write('<input type="text" id="text" name="text" value="'+course.comments.id(comment_id).text+'">'+'<br>')
      res.write('<input type="submit" value="Save Changes">')
      res.write('</form>')
      res.write(' <a href="/templates/homepage.html\">[HOME]</a>' +'<br>');
      res.write(' <a href="/templates/deleteComment/'+courseNum + '/'+comment_id+ '/'+ '\">[DELETE]</a>');
      res.end();
    }
  });
});

/**
 * User story 6
 * Author: Xinran
 * Date modified: Mar 19. 2022
 * edit the comment for a specific course
 * @Param number: the course number for the course (primary key)
 * @Param _id: the id for the comment to be deleted or edited
 */
app.post("/editComment/:number/:comment_id", (req, res) => {
  var courseNum = req.params.number;
  var comment_id = req.params.comment_id;
  var text = req.body.text;
  var rating = req.body.rating;
  var queryObject = { number: courseNum, "comments._id": comment_id };
  if(Number(rating) < 0 || Number(rating) > 5){
    res.type("html").status(200);
    console.log("error: " + err);
    res.write("Rating should be within the range (0,5): " + err);
    res.write(' <a href="/editComment/'+courseNum+'/'+comment_id+'">[BACK]</a>');
    res.end()
  }
  var action = { '$set': { comment_id_str: text, comment_rating_str:rating} };
  Course.find(queryObject, (err, courses) => {
    if (err) {
      res.type("html").status(200);
      console.log("error: " + err);
      res.write("edit comment failed: " + err);
      res.write(' <a href="/editComment/'+courseNum+'/'+comment_id+'">[BACK]</a>');
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end()
    } else if (courses.length == 0) {
      console.log("course not found");
      res.type("html").status(200);
      res.write("Course/comment not found");
      res.write(' <a href="/editComment/'+courseNum+'/'+comment_id+'">[BACK]</a>');
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end()
    } else {
      var course = courses[0]
      course.comments.id(comment_id).rating = Number(rating)
      course.comments.id(comment_id).text = text
      course.save((err, c) => {
        if(err){
          res.type("html").status(200);
          console.log("error: " + err);
          res.write("edit comment failed: " + err);
          res.write(' <a href="/editComment/'+courseNum+'/'+comment_id+'">[BACK]</a>');
          res.write(' <a href="/templates/homepage.html">[HOME]</a>');
          res.end()
        } else {
          res.type("html").status(200);
          res.write("Edit success");
          res.write(' <a href="/viewComments/'+courseNum+'">[BACK]</a>');
          res.write(' <a href="/templates/homepage.html">[HOME]</a>');
          res.end()
        }
      })
    }
  });
});

/**
 * User story 6
 * Author: Xinran
 * Date modified: Mar 19. 2022
 * delete the comment for a specific course
 * @Param number: the course number for the course (primary key)
 * @Param comment_id: the id for the comment to edit
 */
app.get("/deleteComment/:number/:comment_id", (req, res) => {
  var courseNum = req.params.number;
  var comment_id = req.params.comment_id;
  var queryObject = { number: courseNum };
  Course.find(queryObject, (e, courses) => {
    if (e) {
      res.type("html").status(200);
      res.write("Error " + e);
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else if (courses.length == 0) {
      res.type("html").status(200);
      res.write("Course not found");
      res.write(' <a href="/templates/homepage.html">[HOME]</a>');
      res.end();
    } else {
      // unique id for course, thus only one will be returned
      courses[0].comments.id(comment_id).remove(); // delete the comment with id (_id)
      courses[0].save((err) => {
        if (err) {
          res.type("html").status(200);
          res.write("Delete comment failed");
          res.write(' <a href="/templates/homepage.html">[HOME]</a>');
          res.end();
        } else {
          res.type("html").status(200);
          res.write("Delete comment Succeeded");
          res.write(' <a href="/templates/homepage.html">[HOME]</a>');
          res.end();
        }
      });
    }
  });
});
/*************************************************/

app.use("/templates", express.static("templates"));

app.use("/", (req, res) => {
  res.redirect("/templates/homepage.html");
});

app.listen(3000, () => {
  console.log("Listening on port 3000");
});
