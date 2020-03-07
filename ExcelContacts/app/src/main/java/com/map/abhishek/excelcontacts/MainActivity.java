package com.map.abhishek.excelcontacts;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    List<String> contacts;
    Button selectExcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        selectExcel = (Button) findViewById(R.id.selectExcel);
        selectExcel.setOnClickListener(this);

        //if()
        //Request permissions.
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);

        //addContactsExcel(contacts);

    }

    private void addContactsExcel(String Name, String numbermob, String numberwork, String numberhome, String email) {
        try {
            /*ContentResolver cr = this.getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, Name);
            cv.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
            cv.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            cv.put(ContactsContract.Settings.UNGROUPED_VISIBLE,true);
            Uri contact_added = cr.insert(ContactsContract.RawContacts.CONTENT_URI, cv);*/

            /*ContentResolver resolver = getContentResolver();
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

            ContentValues values = new ContentValues();
            long id = ContentUris.parseId(resolver.insert(uri, values));
            uri = Uri.parse("content://com.android.contacts/data");

            values.put("raw_contact_id", id);
            values.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, Name);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            Uri contact_added = resolver.insert(uri, values);*/

            ContentResolver resolver = getContentResolver();
            Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");

            ContentValues values = new ContentValues();
            long id = ContentUris.parseId(resolver.insert(uri, values));
            uri = Uri.parse("content://com.android.contacts/data");

            ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            //.withValue(RawContacts.AGGREGATION_MODE, RawContacts.AGGREGATION_MODE_DEFAULT)
                    .build());

            // first and last names
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, Name)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, "")
                    .build());

            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numbermob)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numberwork)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE)
                    .build());
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numberhome)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());

            try{
                ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, op_list);
            }catch(Exception e){
                e.printStackTrace();
            }

            //int contactId = Integer.valueOf(ContactsContract.RawContacts.CONTENT_URI.toString().substring(ContactsContract.RawContacts.CONTENT_URI.toString().lastIndexOf("/")+1));
            //cr.insert(ContactsContract.Groups.CONTENT_URI, cv).buildUpon();
// add the new contact to myContactsGroup to have it in Contacts Application :


            //ContactsContract.Directory.addToMyContactsGroup(this.getContentResolver(), contactId);

            Toast.makeText(this, "Contact added", Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            TextView tv = new TextView(this);
            tv.setText(e.toString());
            setContentView(tv);
        }
    }

    public List<String> readExcel(String filepath) throws IOException {
        List<String> resultSet = new ArrayList<String>();

        File inputWorkbook = new File("/sdcard/Download/Contacts08_May_2017__05_07_22.xls");
        if(inputWorkbook.exists()){
            Workbook w;
            try {
                w = Workbook.getWorkbook(inputWorkbook);
                // Get the first sheet
                Sheet sheet = w.getSheet(0);
                // Loop over column and lines

                for (int j = 0; j < sheet.getRows(); j++) {
                    Cell cell = sheet.getCell(0, j);
                    //if(cell.getContents().equalsIgnoreCase(key)){
                        //for (int i = 0; i < sheet.getColumns(); i++) {
                            Cell celname = sheet.getCell(1, j);
                            Cell celnumbermob = sheet.getCell(2,j);
                    Cell celnumberwork = sheet.getCell(3,j);
                    Cell celnumberhome = sheet.getCell(4,j);
                    Cell celemail = sheet.getCell(5,j);
                            addContactsExcel(celname.getContents(), celnumbermob.getContents(), celnumberwork.getContents(), celnumberhome.getContents(), celemail.getContents());
                            //resultSet.add(cel.getContents());
                        //}
                    //}
                   // continue;
                }
            } catch (BiffException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            resultSet.add("File not found..!");
            Toast.makeText(this, "File not found..!", Toast.LENGTH_LONG).show();
        }
        if(resultSet.size()==0){
            resultSet.add("Data not found..!");
            Toast.makeText(this, "Data not found..!", Toast.LENGTH_LONG).show();
        }
        return resultSet;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            Uri sheets = data.getData();
            try {
                contacts = readExcel(sheets.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {

        if(view == findViewById(R.id.selectExcel))
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("vnd.ms-excel/*");
            //intent.setType("image/*");

// Do this if you need to be able to open the returned URI as a stream
// (for example here to read the image data).
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            Intent finalIntent = Intent.createChooser(intent, "Select excel sheet");

            startActivityForResult(finalIntent, 1);
        }

    }
}
