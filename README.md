# NeWork

NeWork is a professional social network application for Android.

## Features

- **Authentication & Registration**: Real backend-based login and registration with avatar support.
- **Posts**: View, create, and interact with posts (Likes, Details).
- **Events**: Manage professional events with participation tracking and online/offline status.
- **Users & Profiles**: Explore user list and view detailed profiles with job history.
- **Job Management**: Maintain your own work history within your profile.
- **Rich Media**: Support for images and other attachments.
- **Maps**: Integrated map support for locations (Google Maps).
- **Offline Support**: Local caching using Room database.
- **Localization**: Fully supports English and Russian languages.

## Technologies Used

- **Kotlin** & **Coroutines** / **Flow**
- **Dagger Hilt** (Dependency Injection)
- **Jetpack Navigation** (Single Activity)
- **Retrofit** (Networking)
- **Room** (Database)
- **Glide** (Image Loading)
- **Material Components** (UI)

## Configuration

To build the project, add the following to your `local.properties`:

```properties
API_KEY=your_api_key_here
MAPS_API_KEY=your_google_maps_key_here
```

The API key is required for authentication and maps functionality.

## CI/CD

The project uses GitHub Actions for continuous integration, ensuring build stability and running unit tests on every push.
