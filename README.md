# 353-project

## endpoint explanation:

- /edit/:number

- - This path takes course number as a param and will output the form containing the original information for the course in textField, so that user could modify based on the original value, once submit, the path will post the form and the endpoint will update the data stored in the database

- /addComment/:numberg

- - This path takes course number as a parameter and will output a form for user to fill in the rating and comment on one course. (for tesing purpose only)

- /viewComment/:number

- - This path takes course number as a parameter and will output all comments for one single coursse

- /editComment/:number/:comment_id

- - This path takes course number and comment_id and will output the rating and comments for the specific comment in textfield, so that user could modify based on the value;

- /deleteComment/:number/:comment_id

- - This path takes course number and comment_id and will delete the specific comment