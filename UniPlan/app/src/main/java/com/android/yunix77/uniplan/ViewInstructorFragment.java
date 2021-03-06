package com.android.yunix77.uniplan;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/*
    Fragment for list of Terms
    Includes
        Button to add Term object to database
            on select, opens AddTerm Fragment for data input
        ListView containing start/end dates of each term
            On select List item, opens Courses Fragment listing all courses for selected term
    Note
        Fragment is loaded on startup of app, and on selection of "Outline" from Navigation Drawer
 */
public class ViewInstructorFragment extends Fragment {
    //Fragment layout (UI)
    View myView;
    //Fragment UI components
    ListView instructorDetails;
    //Database
    DatabaseControl db;
    Button addOffice;
    //ID
    int instructor_id;
    //Course Code
    String c_code;
    @Nullable
    @Override
    //Fragment
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inflate Fragment layout
        myView = inflater.inflate(R.layout.fragment_viewinstructor, container, false);
        addOffice = (Button) myView.findViewById(R.id.addoffice);
        //Initialize UI components
        instructorDetails = (ListView) myView.findViewById(R.id.instructordetails);
        //Initialize database
        db = new DatabaseControl(getActivity().getApplicationContext());
        //Get term ID from arguments
        instructor_id = this.getArguments().getInt("INSTRUCTOR_ID");

        setAddOfficeListener();
        //Populate ListView
        SQLiteDatabase readDB = db.helper.getReadableDatabase();
        String query = "SELECT * FROM INSTRUCTOR WHERE _id = ?";
        String[] args = {Integer.toString(instructor_id)};
        final Cursor cursor = readDB.rawQuery(query,args);
        ArrayList<String> instructorStrArr = new ArrayList<String>();
        ArrayAdapter myAdapter;

        cursor.moveToFirst();
        int nameCol = cursor.getColumnIndex("INAME");
        int typeCol = cursor.getColumnIndex("STATUS");
        int emailCol = cursor.getColumnIndex("EMAIL");

        instructorStrArr.add("NAME: " + cursor.getString(nameCol));

        String type;
        switch (cursor.getInt(typeCol)){
            case 0: type = "Professor"; break;
            case 1: type = "T.A."; break;
            default: type = "Unknown"; break;
        }
        instructorStrArr.add("TYPE: " + type);
        instructorStrArr.add("E-MAIL: " + cursor.getString(emailCol));

        myAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, instructorStrArr);
        instructorDetails.setAdapter(myAdapter);

        return myView;
    }

    private void setAddOfficeListener() {
        addOffice.setOnClickListener(new View.OnClickListener(){
            //Switch to AddTerm fragment - i.e. term input screen
            public void onClick(View v){

                Bundle bundle = new Bundle();
                bundle.putInt("INSTRUCTOR_ID", instructor_id);
                AddOfficeFragment addTime = new AddOfficeFragment();
                addTime.setArguments(bundle);
                final FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.include, addTime);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
    }

}
