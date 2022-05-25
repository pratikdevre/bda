package com.pratik.blooddonationapp.Database;

import android.annotation.SuppressLint;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pratik.blooddonationapp.Adapters.MainAdapter;
import com.pratik.blooddonationapp.InterFace.MyCompleteListener;
import com.pratik.blooddonationapp.Models.DonorsModel;
import com.pratik.blooddonationapp.Models.NotificationModel;
import com.pratik.blooddonationapp.Models.ProfileModel;
import com.pratik.blooddonationapp.Models.SentEmailsModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class dbQueries {
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private static final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    static String currentUserId = null;

    public static boolean gotMyProfile = false;

    public static List<DonorsModel> _usersList = new ArrayList<>();
    public static List<DonorsModel> _usersListByLocation = new ArrayList<>();
    public static List<DonorsModel> _usersListByBloodGroup = new ArrayList<>();
    public static List<DonorsModel> _usersListByType = new ArrayList<>();

    public static List<SentEmailsModel> _sentEmailsList = new ArrayList<>();
    public static List<NotificationModel> _notificationList = new ArrayList<>();

    public static DonorsModel _selected_user = new DonorsModel();
    public static ProfileModel profileModel = new ProfileModel();

//    ****************  all static methods  ****************

    //    method to check if user is already logged in or not
    public static boolean checkUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    //    method to sign in user in app
    public static void signInUser(String email, String pass, MyCompleteListener myCompleteListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myCompleteListener.onSuccess();
            }
        })
                .addOnFailureListener(myCompleteListener::onFailure);
    }

    //    method for resetting password forget password
    public static void forgetPassword(String email, MyCompleteListener completeListener) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> completeListener.onSuccess())
                .addOnFailureListener(completeListener::onFailure);
    }

    //    method to sign up new donor user
    public static void signUpDonor(String email, String pass, String name, String phone, String bloodGroup, Uri imgUri, String location, MyCompleteListener myCompleteListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    addUserDataIntoDB(currentUserId, email, name, phone, bloodGroup, imgUri, location, "donor", myCompleteListener);
                }).addOnFailureListener(myCompleteListener::onFailure);

    }

    //    method to sign up the recipient in the data
    public static void signUpRecipient(String email, String pass, String name, String phone, String bloodGroup, Uri imgUri, String location, MyCompleteListener myCompleteListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    addUserDataIntoDB(currentUserId, email, name, phone, bloodGroup, imgUri, location, "recipient", myCompleteListener);
                }).addOnFailureListener(myCompleteListener::onFailure);
    }

    //    method to add the data of donor in database
    private static void addUserDataIntoDB(String currentUserId, String email, String name, String phone, String bloodGroup, Uri imgUri, String location, String type, MyCompleteListener completeListener) {
        Map<String, Object> map = new HashMap<>();
        map.put("UserId", currentUserId);
        map.put("Name", name);
        map.put("Email", email);
        map.put("PhoneNum", phone);
        map.put("Location", location);
        map.put("BloodGroup", bloodGroup);
        map.put("Type", type);
        map.put("Search", type + bloodGroup);

        StorageReference reference = firebaseStorage.getReference().child("ProfilePics/"+currentUserId);

        reference.putFile(imgUri).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
            map.put("ProfilePic", uri);
            firebaseFirestore.collection("donor_recipient_details").document(currentUserId)
                    .set(map)
                    .addOnSuccessListener(unused -> completeListener.onSuccess())
                    .addOnFailureListener(completeListener::onFailure);

        }).addOnFailureListener(completeListener::onFailure)).addOnFailureListener(completeListener::onFailure);

    }

    //    method to fetch all data from database
    public static void getAllUsersInfoFromDB(MyCompleteListener completeListener) {
        _usersList.clear();
        firebaseFirestore.collection("donor_recipient_details")
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            DonorsModel model;
            for (DocumentSnapshot d : queryDocumentSnapshots) {
                model = new DonorsModel(
                        Objects.requireNonNull(d.get("Name")).toString(),
                        Objects.requireNonNull(d.get("PhoneNum")).toString(),
                        Objects.requireNonNull(d.get("Email")).toString(),
                        Objects.requireNonNull(d.get("UserId")).toString(),
                        Objects.requireNonNull(d.get("Location")).toString(),
                        Objects.requireNonNull(d.get("BloodGroup")).toString(),
                        Objects.requireNonNull(d.get("Type")).toString(),
                        Objects.requireNonNull(d.get("Search")).toString(),
                        Objects.requireNonNull(d.get("ProfilePic")).toString()
                );

                if ((model.getCurrentUserId().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))) {
                    profileModel.setName(model.getName());
                    profileModel.setPhone(model.getPhone());
                    profileModel.setEmail(model.getEmail());
                    profileModel.setLocation(model.getLocation());
                    profileModel.setBloodGroup(model.getBloodGroup());
                    profileModel.setType(model.getType());
                    profileModel.setProfilePic(model.getProfilePic());
                    profileModel.setSearch(model.getSearch());
                    gotMyProfile = true;
                } else {
                    _usersList.add(model);
                }
            }
            compatibleWithLoggedUser();
            completeListener.onSuccess();
        })
                .addOnFailureListener(completeListener::onFailure);
    }

    //    method to find compatible with logged user
    public static void compatibleWithLoggedUser() {
        _usersListByType.clear();
        _usersListByLocation.clear();
        _usersListByBloodGroup.clear();
        for (DonorsModel d : _usersList) {
            if (!(d.getType().equals(profileModel.getType()))) {
                _usersListByType.add(d);
            }
        }
        for (DonorsModel e : _usersListByType) {
            if (e.getLocation().equals(profileModel.getLocation())) {
                _usersListByLocation.add(e);
            }
        }
        for (DonorsModel f : _usersListByType) {
            if (f.getBloodGroup().equals(profileModel.getBloodGroup())) {
                _usersListByBloodGroup.add(f);
            }
        }
    }

    //    method to send message to user
    public static void sendMessageToUser(String receiverId, String msg, MyCompleteListener completeListener) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", msg);
        map.put("sender_email", profileModel.getEmail());
        map.put("timestamp", new Date().getTime());
        firebaseFirestore.collection("donor_recipient_details").document(receiverId)
                .collection("messages")
                .add(map)
                .addOnSuccessListener(unused -> completeListener.onSuccess()).addOnFailureListener(completeListener::onFailure);
    }


    //method to add sent emails info in db
    public static void addToSentEmail(String name, String email, MyCompleteListener completeListener) {
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Email", email);
        map.put("timestamp", new Date().getTime());
        firebaseFirestore.collection("donor_recipient_details").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("sent_emails")
                .add(map)
                .addOnSuccessListener(unused -> completeListener.onSuccess()).addOnFailureListener(completeListener::onFailure);

    }


    //method to fetch sent emails info in db
    public static void fetchSentEmails(MyCompleteListener completeListener) {
        firebaseFirestore.collection("donor_recipient_details")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("sent_emails")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    _sentEmailsList.clear();
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        _sentEmailsList.add(new SentEmailsModel(
                                Objects.requireNonNull(d.get("Name")).toString(),
                                Objects.requireNonNull(d.get("Email")).toString()));
                    }
                    completeListener.onSuccess();
                })
                .addOnFailureListener(completeListener::onFailure);

    }

    //    method to get notification from db
    public static void getNotifications(MyCompleteListener completeListener) {
        firebaseFirestore.collection("donor_recipient_details")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("messages")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    _notificationList.clear();
                    for (DocumentSnapshot d : queryDocumentSnapshots) {
                        _notificationList.add(new NotificationModel(
                                Objects.requireNonNull(d.get("sender_email")).toString(),
                                Objects.requireNonNull(d.get("message")).toString())
                        );
                    }
                    completeListener.onSuccess();

                })
                .addOnFailureListener(completeListener::onFailure);
    }


    //    method to logout the logged user
    public static void logoutLoggedUser() {
        _usersList.clear();
        MainAdapter.list.clear();
        currentUserId = null;
        FirebaseAuth.getInstance().signOut();
    }
}
