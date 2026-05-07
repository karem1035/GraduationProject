# Swagger Enum Enhancement - Summary for Frontend Developers

## Overview

We've enhanced the Swagger/OpenAPI documentation with comprehensive enum examples and created a detailed enum reference guide. This document summarizes the changes and how they benefit frontend development.

---

## What's New?

### 1. **Comprehensive Enum Reference Guide** 📚
Created `ENUM_REFERENCE.md` - A complete reference for all 7 enums used in the API.

**Location:** `Toda/ENUM_REFERENCE.md`

**Contents:**
- Detailed documentation for each enum
- Valid values and usage context
- API endpoints where each enum is used
- Status transition diagrams
- Best practices for frontend developers
- Code examples for common scenarios

### 2. **Enhanced Swagger Documentation** 📝
Added `@Schema` annotations with enum examples to all relevant endpoints and DTOs.

**Updated Controllers:**
- ✅ `TripController` - TripStatus enum
- ✅ `BookingController` - RequestStatus enum
- ✅ `TourGuideController` - Import statements for enums
- ✅ `TouristProfileController` - TouristType enum (already had schemas)
- ✅ `authController` - Role enum

**Updated DTOs:**
- ✅ `RegisterRequest` - Role enum with validation
- ✅ `TourGuideProfessionalInfoRequest` - GuideTypeCategory enum
- ✅ `TourGuideDetailsInfoRequest` - TourType enum
- ✅ `TouristBasicInfoRequest` - TouristType enum (already had schemas)

---

## Enums Overview

| Enum | Values | Purpose |
|------|--------|---------|
| **Role** | ADMIN, TOURIST, TOURGUIDE | User authentication and authorization |
| **TripStatus** | NEW, UPCOMING, COMPLETED, CANCELLED | Trip lifecycle management |
| **RequestStatus** | PENDING, ACCEPTED, DECLINED, COMPLETED | Booking request workflow |
| **TouristType** | MALE, FEMALE | Tourist gender preference |
| **GuideType** | MALE, FEMALE | Tour guide gender |
| **GuideTypeCategory** | LICENSED_GUIDE, LOCAL_GUIDE | Professional certification level |
| **TourType** | GROUP, PRIVATE | Tour delivery format |

---

## How to Use the Enhanced Swagger

### Access Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Benefits in Swagger UI

#### 1. **Enum Dropdowns**
Swagger UI now shows dropdown selectors for enum fields with all valid values:
- ✅ No more guessing valid enum values
- ✅ Auto-complete suggestions
- ✅ Clear enumeration of all options

#### 2. **Inline Examples**
Each enum field now shows example values:
```json
{
  "role": "TOURIST",           // Example shown
  "status": "UPCOMING",        // Example shown
  "guideType": "LICENSED_GUIDE" // Example shown
}
```

#### 3. **Allowable Values Documentation**
Swagger schema explicitly lists all allowable values:
```
Schema:
  type: string
  enum: ["ADMIN", "TOURIST", "TOURGUIDE"]
  example: "TOURIST"
```

---

## API Endpoints with Enum Enhancements

### Authentication & Registration
**`POST /api/auth/signup`**
- **Enum:** `Role`
- **Values:** ADMIN, TOURIST, TOURGUIDE
- **Documentation:** Complete with examples and validation

### Trip Management
**`PATCH /api/v1/trip/{tripId}/status`**
- **Enum:** `TripStatus`
- **Values:** UPCOMING, COMPLETED, CANCELLED
- **Documentation:** Valid transitions explained

**`GET /api/v1/trip/guideTrips`**
- **Enum:** `TripStatus` (optional filter)
- **Values:** NEW, UPCOMING, COMPLETED, CANCELLED

### Booking Management
**`GET /api/v1/tourist/bookings`**
- **Enum:** `RequestStatus` (optional filter)
- **Values:** PENDING, ACCEPTED, DECLINED, COMPLETED

### Tour Guide Profiles
**`POST /api/tourguide/profile/professional-info`**
- **Enum:** `GuideTypeCategory`
- **Values:** LICENSED_GUIDE, LOCAL_GUIDE

**`POST /api/tourguide/profile/tour-details`**
- **Enum:** `TourType`
- **Values:** GROUP, PRIVATE

### Tourist Profiles
**`POST /api/tourist/profile/basic-info`**
- **Enum:** `TouristType`
- **Values:** MALE, FEMALE

---

## Frontend Development Best Practices

### 1. **Use Exact Enum Values**
Always use the exact enum values as shown in Swagger. Case-sensitive matching is used.

```javascript
// ✅ CORRECT
const role = "TOURIST";
const status = "UPCOMING";

// ❌ WRONG
const role = "Tourist";
const status = "upcoming";
```

### 2. **Implement Enum Constants**
Create enum constants in your frontend code for type safety:

```typescript
// TypeScript example
export enum Role {
  ADMIN = 'ADMIN',
  TOURIST = 'TOURIST',
  TOURGUIDE = 'TOURGUIDE'
}

export enum TripStatus {
  NEW = 'NEW',
  UPCOMING = 'UPCOMING',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export enum RequestStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  DECLINED = 'DECLINED',
  COMPLETED = 'COMPLETED'
}
```

### 3. **Format Enums for Display**
Convert enum values to user-friendly text:

```javascript
const formatEnum = (value) => {
  return value
    .toLowerCase()
    .split('_')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
};

// Examples:
// "LICENSED_GUIDE" → "Licensed Guide"
// "PENDING" → "Pending"
// "TOURIST" → "Tourist"
```

### 4. **Handle Status-Based UI Logic**
Use enum values to conditionally show/hide UI elements:

```javascript
const getBookingActions = (status) => {
  switch (status) {
    case 'PENDING':
      return ['Cancel'];
    case 'ACCEPTED':
      return ['View Details'];
    case 'COMPLETED':
      return ['Leave Review'];
    case 'DECLINED':
      return ['Book Another'];
    default:
      return [];
  }
};
```

### 5. **Validate User Input**
When creating forms, validate against enum values:

```javascript
const isValidRole = (role) => {
  const validRoles = ['ADMIN', 'TOURIST', 'TOURGUIDE'];
  return validRoles.includes(role);
};
```

---

## Testing with Updated Swagger

### 1. **Try Different Enum Values**
Use Swagger UI to test with different enum values:
- Change `status` to see different trip states
- Test booking workflows with different request statuses
- Create profiles with different guide/tourist types

### 2. **Validate Response Structures**
Check that responses contain correct enum values:
```json
{
  "data": {
    "status": "UPCOMING",
    "guide": {
      "guideType": "LICENSED_GUIDE"
    }
  }
}
```

### 3. **Error Handling**
Swagger now provides clear enum validation errors:
```json
{
  "message": "Invalid value for role. Must be one of: ADMIN, TOURIST, TOURGUIDE"
}
```

---

## Common Enum Workflows

### Trip Status Lifecycle
```
NEW → UPCOMING → COMPLETED
  ↓        ↓
CANCELLED (from NEW or UPCOMING)
```

### Booking Request Flow
```
PENDING → ACCEPTED → COMPLETED
   ↓
DECLINED
```

### User Registration
```
Choose Role (TOURIST/TOURGUIDE/ADMIN)
  ↓
Complete Profile
  ↓
Create/Book Trips
```

---

## Troubleshooting

### Issue: "Invalid enum value" error
**Solution:** Check that you're using the exact enum value (case-sensitive). Refer to `ENUM_REFERENCE.md` or Swagger UI for valid values.

### Issue: Status transition not allowed
**Solution:** Refer to the valid status transitions in `ENUM_REFERENCE.md`. For example, you can't change COMPLETED back to UPCOMING.

### Issue: Missing enum values in dropdown
**Solution:** Refresh the Swagger UI page to load the latest schema changes.

---

## Documentation Files

1. **`ENUM_REFERENCE.md`** - Complete enum reference guide
2. **`SWAGGER_ENUM_UPDATE_SUMMARY.md`** - This file
3. **`api_guide.md`** - General API guide
4. **Swagger UI** - Interactive API documentation at `/swagger-ui.html`

---

## Support & Questions

For questions about enum usage:
1. Check `ENUM_REFERENCE.md` first
2. Review Swagger UI for endpoint-specific enum usage
3. Contact the backend development team

---

## Changelog

### 2026-05-07 - Initial Enhancement
- ✅ Created comprehensive enum reference guide
- ✅ Added enum schemas to all controllers
- ✅ Added enum examples to DTOs
- ✅ Enhanced Swagger UI documentation
- ✅ Added validation annotations to enums

---

**Last Updated:** 2026-05-07  
**API Version:** 1.0  
**Swagger Version:** OpenAPI 3.0