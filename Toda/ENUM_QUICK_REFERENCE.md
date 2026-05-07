# Enum Quick Reference Card

## Quick Lookup Table

| Enum | Values | Used In |
|------|--------|----------|
| **Role** | ADMIN, TOURIST, TOURGUIDE | User registration, authentication |
| **TripStatus** | NEW, UPCOMING, COMPLETED, CANCELLED | Trip management, filtering |
| **RequestStatus** | PENDING, ACCEPTED, DECLINED, COMPLETED | Booking workflow |
| **TouristType** | MALE, FEMALE | Tourist profiles |
| **GuideType** | MALE, FEMALE | Tour guide profiles |
| **GuideTypeCategory** | LICENSED_GUIDE, LOCAL_GUIDE | Tour guide certification |
| **TourType** | GROUP, PRIVATE | Tour format |

---

## Common Enum Values Copy-Paste

```javascript
// Role
['ADMIN', 'TOURIST', 'TOURGUIDE']

// TripStatus
['NEW', 'UPCOMING', 'COMPLETED', 'CANCELLED']

// RequestStatus
['PENDING', 'ACCEPTED', 'DECLINED', 'COMPLETED']

// TouristType & GuideType
['MALE', 'FEMALE']

// GuideTypeCategory
['LICENSED_GUIDE', 'LOCAL_GUIDE']

// TourType
['GROUP', 'PRIVATE']
```

---

## Status Transitions

### Trip Status
```
NEW → UPCOMING → COMPLETED
  ↓        ↓
CANCELLED (from NEW or UPCOMING)
```

### Booking Request
```
PENDING → ACCEPTED → COMPLETED
   ↓
DECLINED
```

---

## TypeScript Enums

```typescript
enum Role {
  ADMIN = 'ADMIN',
  TOURIST = 'TOURIST',
  TOURGUIDE = 'TOURGUIDE'
}

enum TripStatus {
  NEW = 'NEW',
  UPCOMING = 'UPCOMING',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

enum RequestStatus {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  DECLINED = 'DECLINED',
  COMPLETED = 'COMPLETED'
}

enum TouristType {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

enum GuideType {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

enum GuideTypeCategory {
  LICENSED_GUIDE = 'LICENSED_GUIDE',
  LOCAL_GUIDE = 'LOCAL_GUIDE'
}

enum TourType {
  GROUP = 'GROUP',
  PRIVATE = 'PRIVATE'
}
```

---

## Display Formatter

```javascript
const formatEnum = (value) => {
  return value
    .toLowerCase()
    .split('_')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
};

// Usage:
formatEnum('LICENSED_GUIDE')  // "Licensed Guide"
formatEnum('TOURIST')         // "Tourist"
formatEnum('PENDING')         // "Pending"
```

---

## Validation Helper

```javascript
const isValidEnum = (value, enumValues) => {
  return enumValues.includes(value);
};

// Usage:
const role = 'TOURIST';
const validRoles = ['ADMIN', 'TOURIST', 'TOURGUIDE'];
isValidEnum(role, validRoles); // true
```

---

## Color Coding Suggestions

```css
/* Status Colors */
.status-NEW { color: #6c757d; }          /* Gray */
.status-UPCOMING { color: #007bff; }     /* Blue */
.status-COMPLETED { color: #28a745; }    /* Green */
.status-CANCELLED { color: #dc3545; }    /* Red */

.status-PENDING { color: #ffc107; }       /* Yellow */
.status-ACCEPTED { color: #17a2b8; }     /* Cyan */
.status-DECLINED { color: #dc3545; }     /* Red */

/* Type Colors */
.type-TOURIST { color: #007bff; }        /* Blue */
.type-TOURGUIDE { color: #28a745; }      /* Green */

.guide-LICENSED { color: #17a2b8; }     /* Cyan */
.guide-LOCAL { color: #6f42c1; }        /* Purple */
```

---

## Quick API Examples

### Register User
```json
POST /api/auth/signup
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "role": "TOURIST"
}
```

### Update Trip Status
```json
PATCH /api/v1/trip/1/status?status=UPCOMING
```

### Filter Bookings
```json
GET /api/v1/tourist/bookings?status=PENDING
```

### Create Tour Guide Profile
```json
POST /api/tourguide/profile/professional-info
{
  "guideType": "LICENSED_GUIDE",
  "licensedNumber": "LIC-12345",
  "yearsOfExperience": 5,
  "specialization": ["History", "Architecture"]
}
```

### Set Tour Type
```json
POST /api/tourguide/profile/tour-details
{
  "tourType": "PRIVATE",
  "coveredArea": "Downtown",
  "tourDuration": 4
}
```

---

## Common Pitfalls

❌ **Wrong:**
```javascript
const status = 'upcoming';        // lowercase
const role = 'Tourist';           // Title case
const type = 'licensed guide';     // lowercase & space
```

✅ **Right:**
```javascript
const status = 'UPCOMING';        // UPPERCASE
const role = 'TOURIST';           // UPPERCASE
const type = 'LICENSED_GUIDE';     // UPPERCASE & underscore
```

---

## Endpoints by Enum

### Role
- `POST /api/auth/signup`
- `GET /api/auth/me`

### TripStatus
- `PATCH /api/v1/trip/{id}/status`
- `GET /api/v1/trip/guideTrips`

### RequestStatus
- `GET /api/v1/requests/pending`
- `GET /api/v1/requests/accepted`
- `GET /api/v1/tourist/bookings`

### TouristType
- `POST /api/tourist/profile/basic-info`

### GuideTypeCategory
- `POST /api/tourguide/profile/professional-info`

### TourType
- `POST /api/tourguide/profile/tour-details`

---

## Documentation Links

📚 **Full Reference:** `ENUM_REFERENCE.md`  
📝 **Enhancement Summary:** `SWAGGER_ENUM_UPDATE_SUMMARY.md`  
🌐 **Swagger UI:** `/swagger-ui.html`  
📖 **API Guide:** `api_guide.md`

---

**Last Updated:** 2026-05-07