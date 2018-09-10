package com.example.mohamedelwarraky.students;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohamedelwarraky.students.data.StudentContract.StudentEntry;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class MainActivity extends AppCompatActivity {


    // Get name from user.
    EditText mEditText;

    // Id of required student.
    static int currentID;

    String input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mEditText = findViewById(R.id.edit_search_student);

        ImageView SearchImageView = findViewById(R.id.search_student);

        SearchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("MainActivity", "Searching for your student");
                displayDatabaseInfo();
            }
        });


        ImageView EditImageView = findViewById(R.id.edit_student);

        EditImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Uri currentStudentUri = null;

                // Convert name to string and make as lower case
                input = mEditText.getText().toString().trim();

                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                String[] projection = {
                        StudentEntry._ID};

                // Perform a query on the provider using the ContentResolver.
                // Use the {@link StudentEntry#CONTENT_URI} to access the student data.
                Cursor cursor = getContentResolver().query(
                        StudentEntry.CONTENT_URI,   // The content URI of the words table
                        projection,             // The columns to return for each row
                        null,                   // Selection criteria
                        null,                   // Selection criteria
                        null);

                try {

                    // Figure out the index of each column
                    int idColumnIndex = cursor.getColumnIndex(StudentEntry._ID);


                    // Iterate through all the returned rows in the cursor
                    while (cursor.moveToNext()) {

                        currentID = cursor.getInt(idColumnIndex);
                        // Use that index to extract the String or Int value of the word
                        // at the current row the cursor is on.

                        if (currentID == Integer.parseInt(input)) {
                            // Form the content URI that represents the specific student that was clicked on,
                            // by appending the "id" (passed as input to this method) onto the
                            // {@link PetEntry#CONTENT_URI}.
                            // For example, the URI would be "content://com.example.android.students/students/2"
                            // if the pet with ID 2 was clicked on.
                            currentStudentUri = ContentUris.withAppendedId(StudentEntry.CONTENT_URI, currentID);


                        }
                    }
                } finally {
                    // Always close the cursor when you're done reading from it. This releases all its
                    // resources and makes it invalid
                    Log.v("MainActivity", "close cursor");
                    cursor.close();

                }
                // Set the URI on the data field of the intent
                intent.setData(currentStudentUri);

                // Launch the {@link EditorActivity} to display the data for the current student.
                startActivity(intent);
            }
        });

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {

        // Convert name to string and make as lower case
        input = mEditText.getText().toString().trim();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                StudentEntry._ID,
                StudentEntry.COLUMN_STUDENT_NAME,
                StudentEntry.COLUMN_STUDENT_GROUP,
                StudentEntry.COLUMN_STUDENT_DEGREE,
                StudentEntry.COLUMN_STUDENT_PRESENCE};

        // Perform a query on the provider using the ContentResolver.
        // Use the {@link StudentEntry#CONTENT_URI} to access the student data.
        Cursor cursor = getContentResolver().query(
                StudentEntry.CONTENT_URI,   // The content URI of the words table
                projection,             // The columns to return for each row
                null,                   // Selection criteria
                null,                   // Selection criteria
                null);                  // The sort order for the returned rows

        TextView displayView = findViewById(R.id.text_view_student);


        try {
            // Create a header in the Text View that looks like this:
            //
            // The students table contains <number of rows in Cursor> students.
            // _id - name - Group - degree - presence
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.


            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(StudentEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_NAME);
            int groupColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_GROUP);
            int degreeColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_DEGREE);
            int presenceColumnIndex = cursor.getColumnIndex(StudentEntry.COLUMN_STUDENT_PRESENCE);

            // Iterate through all the returned rows in the cursor
            cursor.moveToFirst();
            Log.v("MainActivity", "move to first");
            do {

                Log.v("MainActivity", "searching");

                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                currentID = cursor.getInt(idColumnIndex);
                if (currentID == Integer.parseInt(input)) {
                    Log.v("MainActivity", "found you student");
                    String currentName = cursor.getString(nameColumnIndex).toLowerCase().trim();
                    String currentGroup = cursor.getString(groupColumnIndex);
                    String currentDegree = cursor.getString(degreeColumnIndex);
                    int currentPresence = cursor.getInt(presenceColumnIndex);

                    // Display the values from each column of the current row in the cursor in the TextView

                    displayView.setText("Student ID : " + currentID + "\n" +
                            "Student name : " + currentName + "\n" +
                            "Studdent group : " + currentGroup + "\n");

                    String[] Degree = convertStringToArray(currentDegree);
                    if (Degree.length > 0) {
                        displayView.append("First degree:" + Degree[0] + "\n");
                    } else
                        displayView.append("First degree:" + "\n");
                    if (Degree.length > 1) {
                        displayView.append("Second degree:" + Degree[1] + "\n");
                    } else
                        displayView.append("Second degree : " + "\n");
                    if (Degree.length > 2) {
                        displayView.append("Third degree : " + Degree[2] + "\n");
                    } else
                        displayView.append("Third degree : " + "\n");
                    if (Degree.length > 3) {
                        displayView.append("Fourth degree : " + Degree[3] + "\n");
                    } else
                        displayView.append("Fourth degree : " + "\n");

                    switch (currentPresence) {
                        case StudentEntry.ONE_DAY:
                            displayView.append("Presence : " + "One day" + "\n");
                            break;
                        case StudentEntry.TWO_DAYS:
                            displayView.append("Presence : " + "Two days" + "\n");
                            break;
                        case StudentEntry.THREE_DAYS:
                            displayView.append("Presence : " + "Three days" + "\n");
                            break;
                        case StudentEntry.FOUR_DAYS:
                            displayView.append("Presence : " + "Four days" + "\n");
                            break;
                        default:
                            displayView.append("Presence : " + "Unknown" + "\n");
                            break;
                    }

                    String text = "\n\nThe students table contains " + cursor.getCount() + " students.\n";
                    displayView.append(text);


                }
            } while (cursor.moveToNext());
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid

            Log.v("MainActivity", "close cursor");
            cursor.close();

        }
    }

    /**
     * Helper method to insert hardcoded student data into the database. For debugging purposes only.
     */
    private void insertStudent() {
        // Create a ContentValues object where column names are the keys,
        // and Mohamed's student attributes are the values.
        ContentValues values = new ContentValues();
        values.put(StudentEntry.COLUMN_STUDENT_NAME, "Mohamed Ahmed");
        values.put(StudentEntry.COLUMN_STUDENT_GROUP, "Group A");
        values.put(StudentEntry.COLUMN_STUDENT_PRESENCE, StudentEntry.TWO_DAYS);
        values.put(StudentEntry.COLUMN_STUDENT_DEGREE, "50,45");

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(StudentEntry.CONTENT_URI, values);
    }

    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(StudentEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertStudent();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String[] convertStringToArray(String str) {
        String strSeparator = ",";

        String[] arr = str.split(strSeparator);
        return arr;
    }

}
