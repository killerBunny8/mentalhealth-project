# mentalhealf
## Overview
This project is a mental health tracking application developed for Android using Java and Firebase. The app aims to support users in tracking their moods, identifying patterns, and accessing mindfulness tools. It includes features such as journaling, trend visualization, and personalized feedback based on user logs.

## Features


### Directories 
- **`com.example.mentalhealf/`**: Main directory.
    - **`ActivityFeedback.java`**: Feedback activity.
    - **`ActivityHome.java`**: Mood logging screen with affirmations.
    - **`ActivityJournal.java`**: Journal screen, shows past moodlogs.
    - **`ActivityMain.java`**: Login screen.
    - **`ActivityMeditationHistory.java`**: Meditation history log.
    - **`ActivityMethodMeditation.java`**: Loads meditation fragments, depending on which is chosen.
    - **`ActivityMindfullness.java`**: Main mindfulness dashboard, shows different mindfulness techniques.
    - **`ActivityPostMeditation.java`**: Summary after session, and logs meditation as a moodlog.
    - **`ActivityPrivacy.java`**: Privacy policy screen, shows privacy policy (https://app.freeprivacypolicy.com/).
    - **`ActivityProfessionalHelp.java`**: External/professional help screen.
    - **`ActivityResetPassword.java`**: Password recovery screen utilising firebase.
    - **`ActivitySettings.java`**: App settings/account details/Export or delete data screen.
    - **`ActivitySignup.java`**: Account creation screen.
    - **`ActivityTrends.java`**: Mood trends and visualizations with further insights and conditional logic.
    - **`Feedback.java`**: Class defining feedback.
    - **`feedbackHelper.java`**: Helper for saving feedback to firestore.
    - **`loginHelper.java`**: Firebase login logic.
    - **`Moodlog.java`**: Class defining moodlogs.
    - **`moodLogHelper.java`**: Firebase logic for moods.
    - **`MoodLogAdapter.java`**: Adapter for mood recycler list.
    - **`MeditationAttentionFragment.java`**: Focused attention meditation.
    - **`MeditationBoxBreathFragment.java`**: Breathing exercise fragment.
    - **`MeditationExerciseFragment.java`**: Physical mindfulness fragment.
    - **`MeditationMantraFragment.java`**: Mantra meditation fragment.
    - **`MeditationMettaFragment.java`**: Loving-kindness meditation.
    - **`MeditationHistoryAdapter.java`**: Adapter for meditation history.
    - **`NavigationBar.java`**: Manages app navigation. 
    - **`passwordHelper.java`**: Password validation/reset logic.
    - **`ReminderMessage.java`**: Defines reminder message.
    - **`reminders.java`**: Handles reminder time logic.
    - **`trendsChart.java`**: mpAndroidchart setup.
    - **`UiAnimations.java`**: Defines different animations.
    
- **`layout/`**: XML files for the layout of various screens.
    - **`activity_feedback.xml`**: Layout for the feedback submission screen.
    - **`activity_home.xml`**: Main layout for logging moods.
    - **`activity_journal.xml`**: Shows past mood logs and journaling features.
    - **`activity_main.xml`**: Layout for login.
    - **`activity_meditation_history.xml`**: Lists completed mindfulness sessions.
    - **`activity_method_meditation.xml`**: Displays selected meditation type.
    - **`activity_mindfullness.xml`**: Menu layout for meditation types.
    - **`activity_post_meditation.xml`**: Summary shown after meditation.
    - **`activity_privacy.xml`**: Shows privacy policy.
    - **`activity_professional_help.xml`**: Provides access to real-world support links.
    - **`activity_reset_password.xml`**: Password recovery page.
    - **`activity_settings.xml`**: layoud for User preferences and settings.
    - **`activity_signup.xml`**: Layout for signup.
    - **`activity_trends.xml`**: Graph-based view of mood trends.
    - **`fragment_meditation_attention.xml`**: Focused attention meditation layout.
    - **`fragment_meditation_box_breath.xml`**: Box breathing meditation.
    - **`fragment_meditation_exercise.xml`**: Light physical mindfulness practice.
    - **`fragment_meditation_mantra.xml`**: Repetitive mantra meditation screen.
    - **`fragment_meditation_metta.xml`**: Loving-kindness (Metta) meditation.
    - **`fragment_navigation_bar.xml`**: Reusable bottom navigation UI.
    - **`meditation_historyu_item.xml`**: List item layout for meditation history.
    - **`moodlogitem.xml`**: Layout for each mood entry shown in a list. 

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






