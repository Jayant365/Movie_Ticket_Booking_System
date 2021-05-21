package com.example.moviebox;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SeatActivity extends AppCompatActivity {

    private final String selectedButtonColor = "#9030E617";
    private final String unSelectedButtonColor = "#61E7DFDF";
    private final String disabledButtonColor = "#B03F3E3E";

    private String movieName;
    private String movieDate = "";

    Calendar today = Calendar.getInstance();
    Calendar chosenDate = Calendar.getInstance();
    DateFormat dateFormat = DateFormat.getDateInstance();
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;

    Button A1, A2, A3, A4, A5;
    Button B1, B2, B3, B4, B5;
    Button C1, C2, C3, C4, C5;
    Button D1, D2, D3, D4, D5;
    Button bookingButton;
    Button datePickerButton;

    private CustomDatabaseHelper customDatabaseHelper;

    List<SeatInfo> multipleSeatsInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        customDatabaseHelper = new CustomDatabaseHelper(this);

        //Get extra bundle
        Bundle bundle = getIntent().getExtras();

        //Check if there is a movie name
        if (bundle != null)
        {
            movieName = bundle.getString("movieName");
        }

        //Initialize all buttons
        initButtons();

        movieDate = dateFormat.format(today.getTime());
        datePickerButton.setText("Chosen date : " + movieDate);

        //Set all booked seats
        refreshSeatStatus();
    }

    //get all booked ticket
    private void refreshSeatStatus() {

        //Set color of all buttons to unselected
        refreshColors();

        List<SeatInfo> tempList = new ArrayList<>();

        //Fetch seats booked for a movie from database.
        tempList = customDatabaseHelper.getAllTicketsForMovie(movieName, movieDate);

        //Set booked seats disable
        for (SeatInfo eachSeat : tempList)
        {
            Button tempButton = findViewById(getResId(eachSeat.getSeatNumber(),R.id.class));
            tempButton.setBackgroundColor(Color.parseColor(disabledButtonColor));
        }
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void initButtons(){
        //Row A
        A1 = findViewById(R.id.A1);
        A2 = findViewById(R.id.A2);
        A3 = findViewById(R.id.A3);
        A4 = findViewById(R.id.A4);
        A5 = findViewById(R.id.A5);
        //Row B
        B1 = findViewById(R.id.B2);
        B2 = findViewById(R.id.B1);
        B3 = findViewById(R.id.B3);
        B4 = findViewById(R.id.B4);
        B5 = findViewById(R.id.B5);
        //Row C
        C1 = findViewById(R.id.C1);
        C2 = findViewById(R.id.C2);
        C3 = findViewById(R.id.C3);
        C4 = findViewById(R.id.C4);
        C5 = findViewById(R.id.C5);
        //Row D
        D1 = findViewById(R.id.D1);
        D2 = findViewById(R.id.D2);
        D3 = findViewById(R.id.D3);
        D4 = findViewById(R.id.D4);
        D5 = findViewById(R.id.D5);

        //Book
        bookingButton = findViewById(R.id.buttonBookTickets);
        datePickerButton = findViewById(R.id.buttonDate);

        bookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!movieDate.isEmpty())
                {
                    bookTickets();
                }
                else
                {
                    Toast.makeText(SeatActivity.this, "Please pick a date for your movie.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
    }

    public void refreshColors()
    {
        A1.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        A2.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        A3.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        A4.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        A5.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        B1.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        B2.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        B3.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        B4.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        B5.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        C1.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        C2.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        C3.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        C4.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        C5.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        D1.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        D2.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        D3.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        D4.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
        D5.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
    }

    public void buttonClick(View view) {

        Button pressedButton = (Button)view;

        //Get selected button's color
        ColorDrawable buttonColor = (ColorDrawable) pressedButton.getBackground();
        int colorId = buttonColor.getColor();

        SeatInfo seatInfo = new SeatInfo();
        //Set button details
        seatInfo.setMovieName(movieName);
        seatInfo.setSeatNumber(pressedButton.getText().toString());
        seatInfo.setMovieDate(movieDate);

        if (colorId == Color.parseColor(unSelectedButtonColor))
        {
            //Change color
            pressedButton.setBackgroundColor(Color.parseColor(selectedButtonColor));
            //Add seat to list
            multipleSeatsInfo.add(seatInfo);
        }
        else if(colorId == Color.parseColor(selectedButtonColor))
        {
            //Change color
            pressedButton.setBackgroundColor(Color.parseColor(unSelectedButtonColor));
            //Remove seat from list
            multipleSeatsInfo.remove(seatInfo);
        }
        else
        {
            Toast.makeText(this, "Seat already booked.", Toast.LENGTH_SHORT).show();
        }
    }


    private void bookTickets() {

        if (multipleSeatsInfo.size() > 0)
        {
            try
            {

                //For each object of seat in list, this will run
                for (SeatInfo seats : multipleSeatsInfo)
                {
                    customDatabaseHelper.bookTicketForSeatAndDate(seats);
                }

                refreshSeatStatus();

                int noOfTicket = multipleSeatsInfo.size();
                multipleSeatsInfo.clear();

                List<String> gg = new ArrayList<>();
                Intent intent = new Intent(SeatActivity.this, PaymentActivity.class);

                intent.putExtra("noOfTickets",noOfTicket);
                startActivity(intent);
            }
            catch (Exception ex)
            {
                Log.e("Movie Box: ", "Error in booking seat " + ex.getMessage());
            }
        }
        else
        {
            Toast.makeText(this, "Please select the seat numbers.", Toast.LENGTH_SHORT).show();
        }

    }

    private void pickDate()
    {
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                chosenDate.set(Calendar.YEAR, year);
                chosenDate.set(Calendar.MONTH, month);
                chosenDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String outputString = "";

                if (chosenDate.compareTo(today) <= 0)
                {
                    Toast.makeText(SeatActivity.this, "Please select a future date.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    movieDate = dateFormat.format(chosenDate.getTime());
                    datePickerButton.setText("Chosen date : " + movieDate);
                    refreshSeatStatus();
                }
            }
        };

        datePickerDialog = new DatePickerDialog
                (SeatActivity.this, dateSetListener, today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH));

        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMinDate(now);
        datePickerDialog.getDatePicker().setMaxDate(now+(1000*60*60*24*7)); //After 7 Days from Now

        //datePickerDialog.getDatePicker().setMaxDate();

        datePickerDialog.show();



    }


}
