# 📒 NoteIt

**NoteIt** is a mobile application built using **Jetpack Compose** that allows users to manage their personal task list efficiently. Users can create, edit, and delete tasks, receive notifications about upcoming deadlines, and organize tasks using categories and attachments.

## 📸 Screenshots

<div align="center">
  <img src="https://github.com/user-attachments/assets/d054d9ea-3ffc-43e3-a080-f343b49dc196" width="45%" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/30b3832a-da2b-4d2f-9f4d-15a3705b6686" width="45%"/>
</div>

<div align="center" style="margin-top: 40px;">
  <img src="https://github.com/user-attachments/assets/cc839b74-c02d-4fbb-8ef1-aecb11a5e0ed" width="45%" style="margin-right: 10px;"/>
  <img src="https://github.com/user-attachments/assets/61aea132-8b1f-4cf7-a4c4-77d7522ae419" width="45%"/>
</div>

## ✅ Features

### 📝 Task Management

- Create new tasks using a **Floating Action Button (FAB)**
- Each task includes:
  - Title
  - Description
  - Creation time
  - Deadline
  - Status: Completed / Not Completed
  - Notification toggle (on/off)
  - Category
  - Attachments (images, files) with indicators in the task list
    - Support for adding, removing, and opening attachments via external apps

### 📱 UI & UX

- Scrollable task list
- Task list sorted by execution time (most urgent tasks on top)
- Task search via search bar above the list
- Task sorting using categories
- Task details view and editing screen
- Indicator in the list for tasks with attachments

### ⚙️ Application Settings

- Option to hide completed tasks
- Filter tasks by selected categories
- Customizable notification timing (how many minutes before the deadline to show a notification)

### 🔔 Notifications

- Notifications about upcoming deadlines
- Notification time is configurable in settings
- Tapping on a notification opens the app and navigates directly to the relevant task's edit screen

### 🗃️ Data Storage

- All task data is stored locally using **SQLite**
- Attachments are stored in the app’s private storage

---

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/masloorzech/NoteIt.git
2. Open the project in Android Studio

3. Build and run the app on an emulator or a physical device

### 📂 Technologies Used
Kotlin
Jetpack Compose
Room
AlarmManager for notifications

screen

### 🧑‍💻 Author
Created by masloorzech
Feel free to contribute or report issues!

This project is licensed under the MIT License – see the LICENSE file for details.
