//package com.example.mycoursesapp;
//
//import android.widget.EditText;
//import android.widget.TextView;
//
//import java.util.concurrent.ConcurrentSkipListMap;
//
//import org.w3c.dom.css.Counter;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Search extends AppCompatActivity {
//    EditText name,number,department,professor;
//    boolean alphabetically=false;
//    Double averageRating;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.search);
//
//
//        //get search query
//        name = (EditText) findViewById(R.id.name);
//        number = (EditText) findViewById(R.id.number);
//        department = (EditText) findViewById(R.id.department);
//        professor = (EditText) findViewById(R.id.professor);
//        AverageRating ar = new AverageRating();
//        averageRating = ar.getAverageRating(number.getText().toString());
//
//    }
//
//    private JSONObject updateSearch(){
//        // create json body object
//        JSONObject jsonBody = new JSONObject();
//        // add search query to json object
//        try {
//            jsonBody.put("name",name.getText().toString());
//            jsonBody.put("number", number.getText().toString());
//            jsonBody.put("department", department.getText().toString());
//            jsonBody.put("professor", professor.getText().toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonBody;
//    }
//
//    private JSONObject parseJson(String jsonStr){
//
//        JSONArray jsonarray = new JSONArray(jsonStr);
//        for (int i = 0; i < jsonarray.length(); i++) {
//            JSONObject jsonobject = jsonarray.getJSONObject(i);
//            String name = jsonobject.getString("name");
//            String number = jsonobject.getString("number");
//        }
//    }
//
//    private List<JSONObject> parseJSONtoList(String jsonStr){
//        ArrayList<JSONObject> courseList = new ArrayList<>();
//
//        if (jsonStr != null)
//        {
//            //---Parsing JSON---//
//            // ArrayList<Course> courseList = new ArrayList<>();
//            try
//            {
//
//                JSONArray jsonArray = new JSONArray(jsonStr);
//
//                for (int i = 0; i < jsonArray.length(); i++)
//                {
//                    //Create a temp course object
//                    // Course course = new Course();
//
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//
//                    //Get employee details
//                    // course.setName(jsonObject.getString("name"));
//                    // course.setNumber(jsonObject.getString("number"));
//
//                    //add to list
//                    // courseList.add(course);
//                    courseList.add(jsonObject);
//                }
//
//            } catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return courseList;
//
//    }
//
///*
//    private List<Course> parseJSONtoCourseList(String jsonStr){
//        ArrayList<Course> courseList = new ArrayList<>();
//
//
//        if (jsonStr != null){
//            //---Parsing JSON---//
//            try
//            {
//
//                JSONArray jsonArray = new JSONArray(jsonStr);
//
//                for (int i = 0; i < jsonArray.length(); i++)
//                {
//                    //Create a temp course object
//                    Course course = new Course();
//
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//
//                    //Get employee details
//                    course.setName(jsonObject.getString("name"));
//                    course.setNumber(jsonObject.getString("number"));
//
//                    //add to list
//                    courseList.add(course);
//                }
//
//            } catch (JSONException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return courseList;
//
//    }
//*/
//    private void displayResponse(ArrayList<JSONObject> courses){
//        String updateTxt="";
//        for (JSONObject course : courses){
//            String _name = (String) course.get("name");
//            String _number = (String) course.get("number");
//            updateTxt += "Course Name: "+_name+"; Course Number: "+_number+"\n";
//        }
//        TextView results = findViewById(R.id.results);
//        results.setText(String.valueOf(updateTxt));
//        TextView averageRatingView = findViewById(R.id.average_rating);
//        averageRatingView.setText(String.valueOf(averageRating));
//
//    }
//
//
//    protected String getJSONStr(){
//        String nameS = ((EditText) findViewById(R.id.name)).getText().toString();
//        String numS = ((EditText) findViewById(R.id.number)).getText().toString();
//        String deptS = ((EditText) findViewById(R.id.department)).getText().toString();
//        String profS = ((EditText) findViewById(R.id.professor)).getText().toString();
//
//        HttpURLConnection conn = null;
//        BufferedReader bufferedReader = null;
//
//        try{
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute( () -> {
//                try {
//                    // assumes that there is a server running on the AVD's host on port 3000
//                    // and that it has a /test endpoint that returns a JSON object with
//                    // a field called "message"
//                    String urlStr = "http://10.0.2.2:3000/searching?name="+nameS+"&number="+numS+"&department="+deptS+"&professor="+profS;
//                    if(alphabetically){
//                        urlStr += "&sort=name";
//                    }
//                    // urlStr = urlStr + "name="+name+"&";
//                    URL url = new URL(urlStr);
//                    // new SendDeviceDetails().execute("http://10.0.2.2:3000/search", jsonBody.toString());//?
//
//
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.connect();
//
//
//                    InputStream inputStream = conn.getInputStream();
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                    StringBuffer stringBuffer = new StringBuffer();
//
//                    String line;
//
//                    while ((line = bufferedReader.readLine()) != null){
//                        stringBuffer.append(line).append("\n");
//                    }
//                    if (stringBuffer.length() == 0){return null;}
//                    else{return stringBuffer.toString();}
//                }catch(JSONException e){
//                    return null;
//                }
//            });
//            executor.awaitTermination(2, TimeUnit.SECONDS);
//
//        } catch (IOException e){return null;}
//        finally{
//            if (conn != null){
//                conn.disconnect();
//            }
//            if (bufferedReader != null){
//                try{
//                    bufferedReader.close();
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public void onSearchButtonClick(View v){
//        // get json body object
//        // JSONObject jsonBody = updateSearch();
//        // pass json body object
//        // get json response
//
//        // parse json response into string
//        String jsonStr = getJSONStr();
//        // parse json response into list
//        List<JSONObject> courses = parseJSONtoList(jsonStr);;
//        // update visuals
//        displayResponse(courses);
//        // give list to new page
//        // launch new page
//        // display
//    }
//    public void onSortButtonClick(View v){
//        alphabetically = !alphabetically;
//    }
//    public void onBackButtonClick(View v){
//        Intent i = new Intent();
//        setResult(RESULT_OK, i);
//        finish();
//    }
//}
