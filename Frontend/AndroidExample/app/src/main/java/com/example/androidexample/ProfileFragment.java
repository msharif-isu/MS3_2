package com.example.androidexample;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
//import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import url.RequestURLs;

public class ProfileFragment extends Fragment {

    //TODO removing friends!
    private ArrayList<UserFriend> friendsList;
    private RecyclerView recyclerView;
    ImageView imgView;
    ImageButton addFriends, editBioButton, editProfilePictureButton;

    Button signOut;
    //Button addFriend = findViewById(R.id.addFriend);
    TextView questionsAnswered, achievementsUnlocked, userBiography, usernameText, friendsListText;
    private String username;

    private int userId;
    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/";
    private SwipeRefreshLayout swipeRefreshLayout;

    private String UPLOAD_URL = RequestURLs.SERVER_HTTP_URL + "/setPfp/";

    private Uri mImageUri, selectedUri;

    private ActivityResultLauncher<String> mGetContent;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");
        userId = prefs.getInt("USER_ID", 0);
        friendsList = new ArrayList<>();
        UPLOAD_URL = UPLOAD_URL + userId;


        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        addFriends = view.findViewById(R.id.addFriends);
        questionsAnswered = view.findViewById(R.id.questionsAnswered);
        achievementsUnlocked = view.findViewById(R.id.AcheivementsUnlocked);
        userBiography = view.findViewById(R.id.userBiography);
        usernameText = view.findViewById(R.id.username);
        friendsListText = view.findViewById(R.id.freindsListText);
        recyclerView = view.findViewById(R.id.friendList);
        editBioButton = view.findViewById(R.id.editBioButton);
        signOut = view.findViewById(R.id.login_btn);

        //Profile Picture
        imgView = view.findViewById(R.id.imgView);
        editProfilePictureButton = view.findViewById(R.id.editProfilePictureButton);
        getProfilePic();

        questionsAnswered.setText("Add Friends");
        achievementsUnlocked.setText("");
        getBio();
//        userBiography.setText(temp);
        usernameText.setText(username);
        //friendsListText.setText("Friends:");


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedUri = uri;
                ImageView imageView = requireView().findViewById(R.id.imgView);
                imageView.setImageURI(uri);
                uploadImage();
            }
        });


        editProfilePictureButton.setOnClickListener(v -> {
            editProfilePicture();
            uploadImage();
        });

        setFriendInfo();
        setAdapter();

        addFriends.setOnClickListener(v -> {
            addFriendsDialog();
        });
        editBioButton.setOnClickListener(v -> {
            editButtonDialog();
        });
        signOut.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setFriendInfo();
                getBio();
//                getProfilePic();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;

    }

    private void editProfilePicture() {
        mGetContent.launch("image/*");
    }


    /**
     * Uploads an image to a remote server using a multipart Volley request.
     *
     * This method creates and executes a multipart request using the Volley library to upload
     * an image to a predefined server endpoint. The image data is sent as a byte array and the
     * request is configured to handle multipart/form-data content type. The server is expected
     * to accept the image with a specific key ("image") in the request.
     *
     */
    private void uploadImage() {
        if (selectedUri != null) {
            byte[] imageData = convertImageUriToBytes(selectedUri);
            if (imageData != null) {
                MultipartRequest multipartRequest = new MultipartRequest(
                        Request.Method.POST,
                        UPLOAD_URL,
                        imageData,
                        response -> {
                            // Handle response
                            if (response != null && !response.isEmpty()) {
                                Toast.makeText(requireContext(), response, Toast.LENGTH_LONG).show();
                                getProfilePic();
                            } else {
                                Toast.makeText(requireContext(), "Unknown response", Toast.LENGTH_LONG).show();
                                Log.e("Upload", "Empty response");
                            }
                            Log.d("Upload", "Response: " + response);
                        },
                        error -> {
                            // Handle error
                            if (error != null && error.getMessage() != null && !error.getMessage().isEmpty()) {
                                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_LONG).show();
                                Log.e("Upload", "Empty error message");
                            }
                            Log.e("Upload", "Error: " + error.getMessage());
                        }
                );
                VolleySingleton.getInstance(requireContext()).addToRequestQueue(multipartRequest);
            } else {
                Log.e("ProfileFragment", "Image data is null");
            }
        } else {
            Log.e("ProfileFragment", "Selected URI is null");
        }
    }


    /**
     * Converts the given image URI to a byte array.
     *
     * This method takes a URI pointing to an image and converts it into a byte array. The conversion
     * involves opening an InputStream from the content resolver using the provided URI, and then
     * reading the content into a byte array. This byte array represents the binary data of the image,
     * which can be used for various purposes such as uploading the image to a server.
     *
     * @param imageUri The URI of the image to be converted. This should be a content URI that points
     *                 to an image resource accessible through the content resolver.
     * @return A byte array representing the image data, or null if the conversion fails.
     * @throws IOException If an I/O error occurs while reading from the InputStream.
     */
    private byte[] convertImageUriToBytes(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            return byteBuffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void editButtonDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.edit_bio);
        dialog.show();

        Button save = dialog.findViewById(R.id.save);
        Button cancelButton = dialog.findViewById(R.id.cancel);
        EditText bioEdit = dialog.findViewById(R.id.bioEdit);

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            if(bioEdit.getText().toString().equals("")) {
                bioEdit.setError("Please enter a bio.");
                return;
            }
            String newBio = bioEdit.getText().toString();
            editBio(newBio);
            dialog.dismiss();
        });


    }

    private void editBio(String newBio) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("bio", newBio);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = backendUrl + "editBio/" + username + "/" + newBio;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    userBiography.setText(newBio);
                    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "find this:" + newBio);
                    getBio();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ProfileActivity", "Error parsing response: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error editing bio: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void addFriendsDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.add_friend);
        dialog.show();

        Button addFriend = dialog.findViewById(R.id.addFriend);
        Button cancelButton = dialog.findViewById(R.id.Cancel);
        EditText friendUsernameEdit = dialog.findViewById(R.id.friendUsernameEdit);

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        addFriend.setOnClickListener(v -> {

            String friend = friendUsernameEdit.getText().toString();
            addFriends(friend, dialog);
            dialog.dismiss();
        });
    }

    private void addFriends(String friendId, Dialog dialog) {
        if (username.equals(friendId)) {
            Toast.makeText(requireContext(), "You can't friend yourself!", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("userId", userId);
                requestBody.put("friendId", friendId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestQueue queue = Volley.newRequestQueue(requireContext());
            String url = backendUrl + userId + "/addFriend/" + friendId;
            Log.e("ProfileActivity", "URL = " + url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String message = response.getString("message");
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        Toast.makeText(requireContext(), "Friend added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ProfileActivity", "Error parsing response: " + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //    Toast.makeText(ProfileActivity.this, "Error adding friend", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "Error adding friend: " + error.getMessage());
                }
            });
            queue.add(jsonObjectRequest);
        }
        setFriendInfo();
    }

    private FriendsListAdapter adapter;

    private void setAdapter() {
        adapter = new FriendsListAdapter(requireContext(), friendsList, new FriendsListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                UserFriend friend = friendsList.get(position);
                String friendName = friend.getUsername();
                friendsList.remove(position);
                removeFriendFromDatabase(friendName); // Remove friend from database
                adapter.notifyItemRemoved(position); // Notify adapter of data change
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void removeFriendFromDatabase(String friendName) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = backendUrl + userId + "/removeFriend/" + friendName; // Adjust the URL according to your backend API
        Log.d("ProfileActivity", "Removing friend from database: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ProfileActivity", "Unfriended " + friendName);
                Toast.makeText(requireContext(), "Unfriended " + friendName, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error removing friend from database: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
        setFriendInfo();
    }

    private void setFriendInfo() {
//        friendsList.add(new UserFriend("Alok1", "This is the real alok", null));
//        friendsList.add(new UserFriend("Alok2", "This is actually the real alok", null));
//        friendsList.add(new UserFriend("Alok3", "Nah, This is the real alok!", null));
//        friendsList.add(new UserFriend("Mahdi", "I will give Owais an A on this demo.", null));
//        friendsList.add(new UserFriend("Alok4", "Nope, its actually me", null));
//        friendsList.add(new UserFriend("Alok5", "Alok4 is lying.", null));
//        friendsList.add(new UserFriend("Osamson", "I agree with Alok5", null));
//        friendsList.add(new UserFriend("Aldaco", "Hello, I am definitly the real Dr. Aldaco", null));
//        friendsList.add(new UserFriend("Alok8", "I am the real alok", null));
//        friendsList.add(new UserFriend("Alok9", "Nah.", null));
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = backendUrl + "friends/" + userId;
        Log.d("ProfileActivity", "Fetching friend details: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jsonResponse = response.toString();
                Log.d("ProfileActivity", "Friend Details JSON Response: " + jsonResponse);
                try {
                    // Clear the existing data in the friendsList. This avoids duplicate entries.
                    friendsList.clear();
                    JSONArray userArray = response.getJSONArray("user");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject userObject = userArray.getJSONObject(i);
                        String username = userObject.getString("username");
                        String bio = userObject.isNull("bio") ? "" : userObject.getString("bio");
                        String filePath = userObject.optString("filePath", "");
                        Log.d("ProfileActivity", "Username: " + username);
                        Log.d("ProfileActivity", "Bio: " + bio);
                        Log.d("ProfileActivity", "File Path: " + filePath);

                        String profilePicUrl = userObject.optString("profilePicUrl", "");

                        friendsList.add(new UserFriend(username, bio, filePath));
                    }

                    // Notify the adapter that the data has changed
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ProfileActivity", "Error parsing friend details JSON: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error fetching friend details: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void getBio() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        String url = backendUrl + "users/getBio/" + username;
        Log.e("ProfileActivity", "Bio url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userBiography.setText(response);
                Log.e("ProfileActivity", "Bio received:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error fetching bio: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Fetches the user's profile picture from the server.
     */
    //backendUrl + "users/" + userId + "/profilePicture"
    private void getProfilePic() {
        // Make a request to fetch the profile picture
        String imageUrl = backendUrl + "images/" + username;
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(100)) // Adjust the corner radius as needed
                .placeholder(R.drawable.profile); // Placeholder image while loading
        Glide.with(requireContext())
                .asBitmap()
                .load(imageUrl)
                .apply(requestOptions)
                .skipMemoryCache(true)  // Disable memory caching
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // Disable disk caching
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Set the retrieved profile picture to the ImageView
                        imgView.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Handle the case where image loading fails
                        imgView.setImageResource(R.drawable.profile);
                    }
                });
    }


}