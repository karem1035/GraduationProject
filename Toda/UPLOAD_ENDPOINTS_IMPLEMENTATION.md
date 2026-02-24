# Tour Guide Upload Endpoints Implementation

## Summary
This document describes the implementation of enhanced upload endpoints for tour guide profiles with full URL support and Swagger UI integration.

## Changes Made

### 1. Configuration Updates

#### application.properties
Added configurable server base URL:
```properties
app.server.base-url=http://localhost:8080
```

This allows the base URL to be easily changed for different environments (dev, staging, production).

### 2. Controller Enhancements

#### TourGuideController.java
- **Injected server base URL** using `@Value("${app.server.base-url}")`
- **Updated `saveFile()` method** to return full URLs instead of relative paths
- **Added Swagger annotations** to all upload endpoints for UI file upload support
- **Added `@SecurityRequirement`** at class level for consistent authentication

**Upload Endpoints Enhanced:**
- `POST /api/tourguide/profile/photo` - Upload profile photo
- `POST /api/tourguide/profile/license` - Upload license document
- `POST /api/tourguide/profile/id` - Upload ID document

**Swagger Features Added:**
- `@Operation` annotations with descriptions
- `@Parameter` annotations for file input
- `@Content` annotations for multipart/form-data
- `@ApiResponses` for proper API documentation
- File upload UI with "Choose File" buttons in Swagger UI

### 3. DTO Updates

#### UserWithProfileResponse.java
Added three new fields to `TourGuideProfileData`:
- `profilePhoto` (String) - Full URL to profile photo
- `license` (String) - Full URL to license document
- `idDocument` (String) - Full URL to ID document

### 4. Service Updates

#### authService.java
Updated `getUserWithProfile()` method to populate the new file URL fields when building the TourGuideProfileData object.

## API Behavior

### Upload Flow
1. User uploads file via Swagger UI or API call
2. File is saved to `uploads/{folder}/{uuid}.{ext}`
3. Full URL is generated: `http://localhost:8080/uploads/{folder}/{uuid}.{ext}`
4. Full URL is stored in database
5. Full URL is returned in response

### /api/auth/me Response
When a tour guide calls `/api/auth/me`, the response now includes:
```json
{
  "user": {
    "id": 1,
    "username": "John Doe",
    "email": "john@example.com",
    "role": "TOURGUIDE"
  },
  "tourGuideProfile": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "city": "Cairo",
    "phone": "+201234567890",
    "licensedNumber": "LIC-12345",
    "yearsOfExperience": 5,
    "guideType": "PROFESSIONAL",
    "tourType": "CULTURAL",
    "coveredArea": "Downtown Cairo",
    "tourDuration": 4,
    "languages": ["ENGLISH", "ARABIC"],
    "profilePhoto": "http://localhost:8080/uploads/profile-photos/550e8400-e29b-41d4-a716-446655440000.jpg",
    "license": "http://localhost:8080/uploads/licenses/660e8400-e29b-41d4-a716-446655440000.pdf",
    "idDocument": "http://localhost:8080/uploads/id-documents/770e8400-e29b-41d4-a716-446655440000.jpg"
  },
  "touristProfile": null
}
```

## Swagger UI Features

### File Upload Interface
Each upload endpoint now provides:
1. **"Try it out" button** to enable the form
2. **"Choose File" button** for file selection
3. **File type validation** - only images and PDFs allowed
4. **Description of supported formats**
5. **Example responses** showing the full URL

### Supported File Types
- **Images**: jpg, png, gif, webp (for profile photo)
- **Documents**: pdf (for license and ID)
- **Max file size**: 10MB (configured in application.properties)

## Static Resource Serving

The application already has proper static resource configuration (WebConfig.java):
```java
registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:uploads/");
```

This ensures files stored in the `uploads/` directory are accessible via HTTP requests.

## Testing

### Manual Testing via Swagger UI
1. Access Swagger UI at `http://localhost:8080/swagger-ui.html`
2. Click on any upload endpoint under `/api/tourguide`
3. Click "Authorize" and enter your JWT token
4. Click "Try it out"
5. Click "Choose File" and select a file
6. Click "Execute"
7. Verify the response contains the full URL

### Testing /api/auth/me
1. Upload files using the upload endpoints
2. Call `GET /api/auth/me` with your JWT token
3. Verify the `tourGuideProfile` object contains the full URLs

## Environment Configuration

To change the base URL for different environments, update `application.properties`:

**Development:**
```properties
app.server.base-url=http://localhost:8080
```

**Production:**
```properties
app.server.base-url=https://api.yourdomain.com
```

## Security Considerations

1. **File Type Validation**: Only images and PDFs are allowed
2. **File Size Limit**: 10MB maximum per file
3. **Unique Filenames**: UUID-based filenames prevent collisions
4. **Authentication**: All upload endpoints require valid JWT token
5. **Authorization**: Users can only upload to their own profiles

## File Structure

Uploaded files are organized as:
```
uploads/
├── profile-photos/
│   ├── {uuid}.jpg
│   ├── {uuid}.png
│   └── ...
├── licenses/
│   ├── {uuid}.pdf
│   └── ...
└── id-documents/
    ├── {uuid}.jpg
    ├── {uuid}.pdf
    └── ...
```

## Future Enhancements

Potential improvements:
1. Add image compression/optimization
2. Add file metadata (size, type, upload date)
3. Implement file CDN integration
4. Add file expiration/deletion policies
5. Add virus scanning for uploaded files
6. Implement image thumbnails for profile photos