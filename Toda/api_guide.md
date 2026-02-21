# Tour Guide File Management API Guide

## 1. Overview
This API provides endpoints to upload and delete 3 types of files for a Tour Guide:
- **Profile Photo**
- **License Document**
- **ID Document**

*Note: The API automatically detects which Tour Guide is uploading/deleting the file based on the JWT `Authorization` token provided in the header. You do not need to send the Tour Guide's ID.*

## 2. Uploading Files

To upload a file, send a `multipart/form-data` request.

### Endpoints
- **Profile Photo**: `POST /api/tourguide/profile/photo`
- **License Document**: `POST /api/tourguide/profile/license`
- **ID Document**: `POST /api/tourguide/profile/id`

### Request Format
- **Method**: `POST`
- **Headers**:
  - `Content-Type: multipart/form-data`
  - `Authorization: Bearer <token>`
- **Body**:
  - Key: `file`
  - Value: The file object from the file input element.

### Frontend Example (React/Fetch)
```javascript
const uploadFile = async (fileType, file) => {
  const formData = new FormData();
  formData.append('file', file);

  const endpointMap = {
    photo: `/api/tourguide/profile/photo`,
    license: `/api/tourguide/profile/license`,
    id: `/api/tourguide/profile/id`,
  };

  try {
    const response = await fetch(endpointMap[fileType], {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
      body: formData,
    });

    const data = await response.json();
    return data.data; // Returns relative URL (e.g., "/uploads/profile-photos/image.jpg")
  } catch (error) {
    console.error('Upload Error:', error);
  }
};
```

## 3. Displaying Uploaded Images

The backend serves the images statically. This means the URL returned by the upload endpoint can be right into an `<img>` tag.

### Example
```jsx
const backendUrl = "http://localhost:8080";

<img 
  src={`${backendUrl}${tourGuideProfile.profilePhoto}`} 
  alt="Profile Photo" 
/>
```

## 4. Deleting Files

### Endpoints
- **Profile Photo**: `DELETE /api/tourguide/profile/photo`
- **License Document**: `DELETE /api/tourguide/profile/license`
- **ID Document**: `DELETE /api/tourguide/profile/id`

### Request Format
- **Method**: `DELETE`
- **Headers**:
  - `Authorization: Bearer <token>`

### Frontend Example (React/Fetch)
```javascript
const deleteFile = async (fileType) => {
  const endpointMap = {
    photo: `/api/tourguide/profile/photo`,
    license: `/api/tourguide/profile/license`,
    id: `/api/tourguide/profile/id`,
  };

  try {
    const response = await fetch(endpointMap[fileType], {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    });

    const data = await response.json();
    console.log('Delete Success:', data);
  } catch (error) {
    console.error('Delete Error:', error);
  }
};
```
