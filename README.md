# mentalhealf
## Overview
This project is a mental health tracking application developed for Android using Java and Firebase. The app aims to support users in tracking their moods, identifying patterns, and accessing mindfulness tools. It includes features such as journaling, trend visualization, and personalized feedback based on user logs.

## Features


### Directories
- **`com.example.mentalhealf/`**: Main directory.
- 
    
- **`layout/`**: XML files for the layout of various screens.
- 

## Setup

### Prerequisites
- **Android Studio**: Required for development and building the app.
- **Java Development Kit (JDK)**: Ensure you have the JDK set up for compiling Java code.
- **Api Level**: Only tested on Api 34, did not work on Api 29, however the target Api is 31.
- **Internet Connection**: Required for syncing mood logs and feedback.

### Steps to Run the Project
1. Clone the repository or download it as a ZIP.
2. Import the project into Android Studio.
3. Build and run the app on an Android emulator or device.

### Firebase Configuration
- Firebase is used for storing mood entries, journals, and feedback.
- Ensure Firebase Authentication is enabled.
- The app uses Firestore for mood data and real-time syncing.

## Security Features
- **Firebase Authentication**
  - Handles user login and account management securely.
- **Input Validation**
  - All inputs (mood levels, journal entries) are validated for completeness and correctness.
- **Data Integrity**
  - Only authenticated users can access or modify their own data.
- **Secure Firebase Rules**
  - Firestore rules are applied to restrict data access per user.
 

## Testing






