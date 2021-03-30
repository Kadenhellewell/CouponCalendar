package com.kadenhellewellcouponcalendar.phoneapp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kadenhellewellcouponcalendar.api.CouponsAdapter;
import com.kadenhellewellcouponcalendar.api.viewmodels.CouponViewModel;
import com.kadenhellewellcouponcalendar.api.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.Locale;


public class ChangeColorFragment extends Fragment {
    public static final int RECORD_CODE = 1;
    private boolean isTalking = false;

    public ChangeColorFragment() {
        super(R.layout.fragment_change_color);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_CODE);
        SpeechRecognizer recognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        // ((HomeActivity)getActivity()) gets info from the main activity
        ((HomeActivity)getActivity()).toolbar.setBackgroundColor(Color.RED);
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                System.out.println("Error: " + i);
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                //best answer at result.get(0)
                //TODO change color of thing
                int newColor = 0;
                switch (results.get(0))
                {
                    case "blue":
                        newColor = Color.BLUE;
                        break;
                    case "black":
                        newColor = Color.BLACK;
                        break;
                    case "yellow":
                        newColor = Color.YELLOW;
                        break;
                    case "green":
                        newColor = Color.GREEN;
                        break;
                    default:
                        newColor = Color.BLUE;
                }
                ((HomeActivity)getActivity()).toolbar.setBackgroundColor(newColor);
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        view.findViewById(R.id.talk).setOnClickListener((v) -> {
            if(!isTalking) {
                recognizer.startListening(recognizerIntent);
                isTalking = true;
            } else {
                isTalking = false;
                recognizer.stopListening();
            }
        });
    }
}