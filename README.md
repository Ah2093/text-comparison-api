# Text Comparison API

A RESTful API for comparing a reference text file with other `.txt` files in a specified folder using **Jaccard
Similarity**.

---

## 📦 Table of Contents

1. [Overview](#overview)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Endpoints](#endpoints)
7. [Error Handling](#error-handling)

---

## 🌟 Overview

This API allows users to upload a reference `.txt` file and compare it with all `.txt` files in a predefined folder. The
comparison uses **Jaccard Similarity** to calculate the similarity percentage between the reference file and each file
in the folder.

The folder path is configured in the `application.properties` file.

---

## ✨ Features

- **File Upload**: Upload a `.txt` file via a POST request.
- **Validation**: Validates uploaded files (file type, size, and existence).
- **Comparison**: Compares the reference file with other `.txt` files in the folder.
- **Multithreading**: Tokenizes files in parallel for better performance.
- **Error Handling**: Provides meaningful error messages for invalid inputs or server errors.

---

## 🔧 Prerequisites

- Java 17+
- Maven
- Spring Boot 3.x
- Git

---

## 💻 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Ah2093/text-comparison-api
   cd text-comparison-api
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Configure the folder path in `src/main/resources/application.properties`:
   ```properties
   app.comparison.folder-path=./uploads
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

---

## 🚀 Usage

### 1. Start the Application

Ensure the folder specified in `app.comparison.folder-path` exists and contains `.txt` files.

### 2. Test the API

Use tools like **Postman** or **curl** to interact with the API.

---

## 🌐 Endpoints

### POST `/api/v1/text-comparison/compare`

Upload a reference `.txt` file and compare it with other `.txt` files in the folder.

#### Request:

```bash
curl -X POST http://localhost:8080/api/v1/text-comparison/compare \
  -F "referenceFile=@/path/to/reference.txt"
```

#### Response (Success):

```json
{
   "referenceFile": "reference -7941612357454009620.txt",
   "comparisons": {
      "junior java developer vois - Copy (3).txt": "47.42 %",
      "junior java developer vois - Copy (12).txt": "100.00 %",
      "junior java developer vois - Copy (6).txt": "100.00 %",
      "junior java developer vois - Copy (9).txt": "100.00 %",
      "junior java developer vois - Copy (4) - Copy.txt": "47.42 %",
      "junior java developer vois - Copy (10).txt": "100.00 %",
      "junior java developer vois - Copy (3) - Copy.txt": "47.42 %",
      "junior java developer vois - Copy.txt": "90.17 %",
      "junior java developer vois.txt": "100.00 %",
      "junior java developer vois - Copy (8).txt": "100.00 %",
      "junior java developer vois - Copy (5).txt": "100.00 %",
      "junior java developer vois - Copy (7).txt": "100.00 %",
      "junior java developer vois - Copy (4).txt": "90.17 %",
      "junior java developer vois - Copy (2).txt": "90.17 %",
      "junior java developer vois - Copy (11).txt": "100.00 %"
   }
}
```

#### Response (Error):

```json
{
  "message": "Only .txt files are allowed"
}
```

---

## ❗ Error Handling

The API returns meaningful error messages for various scenarios:

| Error Code | Message                     | Description                          |
|------------|-----------------------------|--------------------------------------|
| 400        | Only .txt files are allowed | Uploaded file is not a `.txt` file   |
| 400        | File size exceeds 10MB      | Uploaded file exceeds the size limit |
| 400        | File is empty               | Uploaded file is empty               |
| 404        | Folder does not exist       | Configured folder path is invalid    |
| 500        | Internal Server Error       | Unexpected server-side error         |

---