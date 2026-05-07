# Toda API - Enum Reference Guide

This document provides a comprehensive reference for all enums used in the Toda API. Frontend developers can use this guide to understand valid enum values and their usage contexts.

---

## Table of Contents

1. [Role](#role)
2. [TripStatus](#tripstatus)
3. [RequestStatus](#requeststatus)
4. [TouristType](#touristtype)
5. [GuideType](#guidetype)
6. [GuideTypeCategory](#guidetypecategory)
7. [TourType](#tourtype)

---

## Role

**Purpose:** Defines the user role in the system for authentication and authorization.

**Valid Values:**
- `ADMIN` - System administrator with full access
- `TOURIST` - Regular user who books tours
- `TOURGUIDE` - Tour guide who creates and manages trips

**Usage Context:**
- User registration (`POST /api/auth/signup`)
- Authentication JWT token
- Profile creation and updates

**API Endpoints:**
- `POST /api/auth/signup` - Specify role during registration
- `GET /api/auth/me` - Returns user role in response

**Example:**
```json
{
  "role": "TOURIST"
}
```

---

## TripStatus

**Purpose:** Represents the lifecycle status of a trip throughout its existence.

**Valid Values:**
- `NEW` - Trip created but not yet published/active
- `UPCOMING` - Trip published and available for booking
- `COMPLETED` - Trip has finished successfully
- `CANCELLED` - Trip cancelled by guide or system

**Valid Status Transitions:**
- `NEW` → `UPCOMING` (Publish trip)
- `NEW` → `CANCELLED` (Cancel before publishing)
- `UPCOMING` → `COMPLETED` (Trip finished)
- `UPCOMING` → `CANCELLED` (Cancel active trip)

**Usage Context:**
- Trip creation and management
- Trip filtering and search
- Guide dashboard

**API Endpoints:**
- `PATCH /api/v1/trip/{tripId}/status` - Update trip status
- `GET /api/v1/trip/guideTrips?statusKey={status}` - Filter by status

**Example:**
```json
{
  "status": "UPCOMING"
}
```

**Important Notes:**
- Only the trip owner (guide) can update the status
- Status changes are irreversible (e.g., COMPLETED cannot go back to UPCOMING)
- Trips in `NEW` status are not visible to tourists
- Only `UPCOMING` trips are bookable

---

## RequestStatus

**Purpose:** Tracks the status of booking requests between tourists and tour guides.

**Valid Values:**
- `PENDING` - Initial state, awaiting guide response
- `ACCEPTED` - Guide has accepted the booking request
- `DECLINED` - Guide has rejected the booking request
- `COMPLETED` - Tour has been completed successfully

**Status Workflow:**
1. Tourist creates booking → `PENDING`
2. Guide responds → `ACCEPTED` or `DECLINED`
3. Accepted tours → `COMPLETED` after trip ends

**Usage Context:**
- Booking request management
- Guide request dashboard
- Tourist booking history

**API Endpoints:**
- `GET /api/v1/requests/pending` - Get pending requests (Guide)
- `GET /api/v1/requests/accepted` - Get accepted requests (Guide)
- `PATCH /api/v1/requests/{requestId}/accept` - Accept request (Guide)
- `PATCH /api/v1/requests/{requestId}/decline` - Decline request (Guide)
- `PATCH /api/v1/tourist/bookings/{requestId}/cancel` - Cancel booking (Tourist)
- `GET /api/v1/tourist/bookings?status={status}` - Filter bookings by status (Tourist)

**Example:**
```json
{
  "status": "PENDING"
}
```

**Important Notes:**
- Tourists can cancel `PENDING` requests
- Guides cannot cancel accepted requests (must decline pending ones)
- `COMPLETED` status is set automatically after trip completion

---

## TouristType

**Purpose:** Specifies the gender preference of the tourist for guide matching.

**Valid Values:**
- `MALE` - Male tourist
- `FEMALE` - Female tourist

**Usage Context:**
- Tourist profile creation
- Guide recommendation algorithm

**API Endpoints:**
- `POST /api/tourist/profile/basic-info` - Create/update tourist profile
- `GET /api/tourist/profile/basic-info` - Get tourist profile

**Example:**
```json
{
  "type": "FEMALE"
}
```

---

## GuideType

**Purpose:** Specifies the gender of the tour guide for matching with tourist preferences.

**Valid Values:**
- `MALE` - Male tour guide
- `FEMALE` - Female tour guide

**Usage Context:**
- Tour guide profile creation
- Search and filtering
- Matching tourists with compatible guides

**API Endpoints:**
- `POST /api/tourguide/profile/basic-info` - Create/update guide profile
- `GET /api/tourguide/profile/basic-info` - Get guide profile
- `GET /api/tourguide/profiles` - Search/filter guides

**Example:**
```json
{
  "type": "MALE"
}
```

---

## GuideTypeCategory

**Purpose:** Classifies tour guides based on their professional certification level.

**Valid Values:**
- `LICENSED_GUIDE` - Professional tour guide with official license/certification
- `LOCAL_GUIDE` - Local resident guide with extensive area knowledge but no official license

**Usage Context:**
- Guide profile categorization
- Search and filtering
- Pricing considerations (licensed guides typically charge more)

**API Endpoints:**
- `POST /api/tourguide/profile/professional-info` - Create/update professional info
- `GET /api/tourguide/profile/{id}` - Get guide details
- `GET /api/tourguide/profiles` - Search/filter guides

**Example:**
```json
{
  "guideType": "LICENSED_GUIDE"
}
```

**Important Notes:**
- `LICENSED_GUIDE` requires license document upload
- Both categories can create and manage trips
- This affects search results and pricing expectations

---

## TourType

**Purpose:** Defines the delivery format of tours offered by guides.

**Valid Values:**
- `GROUP` - Tour with multiple tourists (shared experience)
- `PRIVATE` - Private tour for a single group/couple/family

**Usage Context:**
- Guide profile setup
- Trip creation
- Search and filtering
- Pricing structure

**API Endpoints:**
- `POST /api/tourguide/profile/tour-details` - Create/update tour details
- `GET /api/tourguide/profile/{id}` - Get guide details
- `GET /api/v1/trips` - Search trips (filter by tour type)

**Example:**
```json
{
  "tourType": "PRIVATE"
}
```

**Important Notes:**
- `GROUP` tours typically have fixed pricing per person
- `PRIVATE` tours often have fixed total pricing
- Tourists can filter by tour type when searching
- Affects the maximum number of tourists allowed per booking

---

## Quick Reference Summary

| Enum | Values | Usage | Controller |
|------|--------|-------|------------|
| **Role** | ADMIN, TOURIST, TOURGUIDE | User authentication | authController |
| **TripStatus** | NEW, UPCOMING, COMPLETED, CANCELLED | Trip lifecycle | TripController |
| **RequestStatus** | PENDING, ACCEPTED, DECLINED, COMPLETED | Booking workflow | BookingController |
| **TouristType** | MALE, FEMALE | Tourist profile | TouristProfileController |
| **GuideType** | MALE, FEMALE | Guide profile | TourGuideController |
| **GuideTypeCategory** | LICENSED_GUIDE, LOCAL_GUIDE | Guide certification | TourGuideController |
| **TourType** | GROUP, PRIVATE | Tour delivery format | TourGuideController |

---

## Best Practices for Frontend Developers

### 1. **Use Exact Values**
Always use the exact enum values as specified above. Case-sensitive matching is used.

```javascript
// ❌ WRONG
"role": "Tourist"
"tripStatus": "upcoming"

// ✅ CORRECT
"role": "TOURIST"
"tripStatus": "UPCOMING"
```

### 2. **Handle Status Transitions**
When displaying UI elements, consider the valid status transitions:

- Don't show "Accept" button for `ACCEPTED` requests
- Don't show "Complete" button for `PENDING` trips
- Show appropriate actions based on current state

### 3. **Filtering**
When implementing search/filter features:

```javascript
// Example: Filter trips by status
const filterTrips = (trips, status) => {
  return trips.filter(trip => trip.status === status);
};

// Filter by multiple statuses
const filterByStatuses = (trips, statuses) => {
  return trips.filter(trip => statuses.includes(trip.status));
};
```

### 4. **Display Formatting**
Convert enum values to user-friendly display text:

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
// "TRIP_STATUS" → "Trip Status"
// "PENDING" → "Pending"
```

### 5. **Type Safety**
Consider creating TypeScript enums or constants for type safety:

```typescript
// TypeScript example
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
```

---

## Support

For questions or clarifications about enum usage, refer to:
- Swagger UI documentation: `/swagger-ui.html`
- API documentation: `/api_guide.md`
- Contact the backend development team

---

**Last Updated:** 2026-05-07
**API Version:** 1.0