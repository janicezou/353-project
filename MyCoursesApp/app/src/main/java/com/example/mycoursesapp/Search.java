<<<<<<< HEAD
//package com.example.mycoursesapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class Search extends AppCompatActivity {
//    EditText name,number,department,professor;
//
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
//    private void displayResponse(ArrayList<JSONObject> courses){
//        String updateTxt="";
//        for (JSONObject course : courses){
//            String _name = (String) course.get("name");
//            String _number = (String) course.get("number");
//            updateTxt += "Course Name: "+_name+"; Course Number: "+_number+"\n";
//        }
//        TextView results = findViewById(R.id.results);
//        results.setText(String.valueOf(updateTxt));
//
//    }
//}
//    public void onSearchButtonClick(View v){
//        // get json body object
////        JSONObject jsonBody = updateSearch();
//        String nameS = ((EditText) findViewById(R.id.name)).getText().toString();
//        String numS = ((EditText) findViewById(R.id.number)).getText().toString();
//        String deptS = ((EditText) findViewById(R.id.department)).getText().toString();
//        String profS = ((EditText) findViewById(R.id.professor)).getText().toString();
//        ArrayList<JSONObject> courses;
//
//        // connect to search endpoint
//        try {
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            executor.execute( () -> {
//                        try {
//                            // assumes that there is a server running on the AVD's host on port 3000
//                            // and that it has a /test endpoint that returns a JSON object with
//                            // a field called "message"
//                            String urlStr = "http://10.0.2.2:3000/searching?name="+nameS+"&number="+numS+"&department="+deptS+"&professor="+profS;
//                            // urlStr = urlStr + "name="+name+"&";
//                            URL url = new URL(urlStr);
//
////                            new SendDeviceDetails().execute("http://10.0.2.2:3000/search", jsonBody.toString());//?
//
//
//                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                            conn.setRequestMethod("GET");
//                            conn.connect();
//
//                            Scanner in = new Scanner(url.openStream());
//
//                            while (in.hasNext()) {
//                                String line = in.nextLine();
//                                // the rest of this code assumes that the body contains JSON
//                                // first, create the parser
//                                JSONParser parser = new JSONParser();
//
//                                // then, parse the data and create a JSON object for it
//
//                                JSONArray courseJA = (JSONArray) parser.parse(line);
//
//                                Iterator courseIter = courseJA.iterator();
//                                while(productsIter.hasNext()){
//                                    // create (Java) Product objects for each of the JSON objects
//
//                                    JSONObject course = (JSONObject) courseIter.next();
//                                    courses.add(course);
//                            }
//                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                            message = e.toString();
//                        }
//                    }
//            );
//
//            executor.awaitTermination(2, TimeUnit.SECONDS);
//            displayResponse(courses);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            tv.setText(e.toString());
//        }
//
//        // pass json body object
//        // get json response
//
//        // parse json response into list
//        // give list to new page
//        // launch new page
//        // display
//    }
//    public void onBackButtonClick(View v){
//        Intent i = new Intent();
//        setResult(RESULT_OK, i);
//        finish();
//    }
//}
=======
package com.example.mycoursesapp;

import androidx.appcompat.app.AppCompatActivity;

public class Search extends AppCompatActivity {
    EditText name,number,department,professor;
    boolean alphabetically=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        //get search query
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.number);
        department = (EditText) findViewById(R.id.department);
        professor = (EditText) findViewById(R.id.professor);

    }

    private JSONObject updateSearch(){
        // create json body object
        JSONObject jsonBody = new JSONObject();
        // add search query to json object
        try {
            jsonBody.put("name",name.getText().toString());
            jsonBody.put("number", number.getText().toString());
            jsonBody.put("department", department.getText().toString());
            jsonBody.put("professor", professor.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonBody;
    }

    private JSONObject parseJson(String jsonStr){

        JSONArray jsonarray = new JSONArray(jsonStr);
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String name = jsonobject.getString("name");
            String number = jsonobject.getString("number");
        }
    }

    private void displayResponse(ArrayList<JSONObject> courses){
        String updateTxt="";
        for (JSONObject course : courses){
            String _name = (String) course.get("name");
            String _number = (String) course.get("number");
            updateTxt += "Course Name: "+_name+"; Course Number: "+_number+"\n";
        }
        TextView results = findViewById(R.id.results);
        results.setText(String.valueOf(updateTxt));

    }

    public void onSearchButtonClick(View v){
        // get json body object
//        JSONObject jsonBody = updateSearch();
        String nameS = ((EditText) findViewById(R.id.name)).getText().toString();
        String numS = ((EditText) findViewById(R.id.number)).getText().toString();
        String deptS = ((EditText) findViewById(R.id.department)).getText().toString();
        String profS = ((EditText) findViewById(R.id.professor)).getText().toString();
        ArrayList<JSONObject> courses;

        // connect to search endpoint
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    // and that it has a /test endpoint that returns a JSON object with
                    // a field called "message"
                    String urlStr = "http://10.0.2.2:3000/searching?name="+nameS+"&number="+numS+"&department="+deptS+"&professor="+profS;
                    if(alphabetically){
                        urlStr += "&sort=name";
                    }
                    // urlStr = urlStr + "name="+name+"&";
                    URL url = new URL(urlStr);
                    // new SendDeviceDetails().execute("http://10.0.2.2:3000/search", jsonBody.toString());//?


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());

                    while (in.hasNext()) {
                        String line = in.nextLine();
                        // the rest of this code assumes that the body contains JSON
                        // first, create the parser
                        JSONParser parser = new JSONParser();

                        // then, parse the data and create a JSON object for it

                        JSONArray courseJA = (JSONArray) parser.parse(line);

                        Iterator courseIter = courseJA.iterator();
                        while(productsIter.hasNext()){
                            // create (Java) Product objects for each of the JSON objects

                            JSONObject course = (JSONObject) courseIter.next();
                            courses.add(course);
                        }
                    } 
                } catch (Exception e) {
                            e.printStackTrace();
                            message = e.toString();
                }
            });

            executor.awaitTermination(2, TimeUnit.SECONDS);
            displayResponse(courses);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // pass json body object
        // get json response

        // parse json response into list
        // give list to new page
        // launch new page
        // display
    }
    public void onSortButtonClick(View v){
        alphabetically = !alphabetically;
    }
    public void onBackButtonClick(View v){
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }
}
>>>>>>> 8e5ed6edfab1a6413cdcb921315b35254044b6d4
