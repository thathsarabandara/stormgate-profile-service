
# Profile Service - Spring Boot Microservice

This is a Spring Boot-based Profile Microservice handling user profile creation, photo management, custom field management, and profile retrieval.

## 📂 Project Structure

```
src/main/java/com/thathsarabandara/profile_service/
├── controller
│   └── ProfileController.java
├── dtos
│   └── (All DTO classes)
├── exception
│   └── (All Exception classes)
├── service
│   ├── ProfileService.java
│   ├── ProfileUpdateService.java
│   └── ProfileGetService.java
├── model
│   └── (All model classes)
├── repository
│   └── (All repository classes)
├── utils
│   └── (All utility classes)
└── ProfileServiceApplication.java

```

## 📑 API Endpoints

### 📄 Create Profile

**POST** `/api/v1/profile/`  
- Headers: `Tenant-ID`
- Request: `ProfileRequest` (form-data)
- Response: `ProfileResponse`

### 🖼️ Upload Avatar

**POST** `/api/v1/profile/{profileid}/avatar`  
- Headers: `Tenant-ID`
- Request: `ProfilePhotoRequest` (form-data)
- Response: `ProfileResponse`

### ❌ Delete Profile

**DELETE** `/api/v1/profile/{profileid}`  
- Headers: `Tenant-ID`
- Response: `ProfileResponse`

### 📝 Add or Update Custom Fields

**POST** `/api/v1/profile/{profileId}/custom-fields`  
- Headers: `Tenant-ID`
- Request: `ProfileCustomFieldListRequest` (form-data)
- Response: `ProfileResponse`

### 📑 Get Custom Fields by Profile ID

**GET** `/api/v1/profile/{profileId}/custom-fields`  
- Headers: `Tenant-ID`
- Response: List of custom fields

### 📃 Get All Profiles

**GET** `/api/v1/profile/`  
- Headers: `Tenant-ID`
- Query Params: `page`, `size`
- Response: Paginated list of profiles

### 🔍 Get Profile by Profile ID

**GET** `/api/v1/profile/profile/{profileId}`  
- Headers: `Tenant-ID`
- Response: Profile details

### 🔍 Get Profile by User ID

**GET** `/api/v1/profile/user/{userId}`  
- Headers: `Tenant-ID`
- Response: Profile details

## 📦 Tech Stack

- Java 24
- Spring Boot
- Spring Web
- Spring Security (if applicable)
- MySQL
- Maven

## ⚙️ How to Run

1. Clone the repository:
```
git clone https://github.com/thathsarabandara/stormgate-profile-service.git
cd profile-service
```

2. Configure `application.properties` for your database connection and other environment configs.

3. Build and run:
```
./mvnw spring-boot:run
```

## 🤝 Contribution

1. Fork this repository
2. Create a new branch `git checkout -b feature/your-feature`
3. Commit your changes `git commit -m 'Add your feature'`
4. Push to the branch `git push origin feature/your-feature`
5. Open a Pull Request

## 📞 Contact

### Thathsara Bandara
- 📧 [thathsaraBandara.dev](https://portfolio-v1-topaz-ten.vercel.app/)
- 🌐 [LinkedIn - Thathsara Bandara](https://www.linkedin.com/in/thathsara-bandara-b403582a7/)
- 💻 [Github](https://github.com/thathsarabandara)
- ✉️ [Contact Developer](mailto:thathsaraarumapperuma@gmail.com?subject=Profile%20Service%20Support&body=Hello%20I%20need%20help%20with...)

## 📄 License

This project is licensed under the MIT License.