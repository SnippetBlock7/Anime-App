- Assumptions Made
Minimum API Level: The app is developed with minSdk 24 (Android 7.0), assuming a modern user base that can support Java 8+ features and Material 3 components.

Database as Single Source of Truth: It is assumed that the UI should always observe the local Room database. The network layer's only job is to update the database, ensuring the app remains functional regardless of connection stability.

Development Environment: It is assumed the project is built using Android Studio Ladybug (or newer) with JDK 17 or 21, as required by the latest Kotlin 2.1.0 and KSP plugins.

API Structure: The app assumes the Jikan V4 API response structure remains consistent. It assumes score and episodes fields can be null, handled via default "N/A" values in the UI.

- Features Implemented
Reactive MVVM Architecture: Separation of concerns using ViewModel, Repository, and LiveData/Flow to ensure a lifecycle-aware UI.

Offline First functionality: Integration of Room Database allows users to browse previously loaded anime without an active internet connection.

Edge-to-Edge Design: The UI handles Window Insets, ensuring content is perfectly placed below the status bar and above the system navigation buttons.

Clean Architecture: Utilizes the MVVM (Model-View-ViewModel) pattern with a Repository layer to separate concerns.

Modern Image Loading: Integration with Glide for memory-efficient image caching and rounded-corner transformations using ShapeableImageView.

Modern Dependency Management: Uses the Gradle Version Catalog (libs.versions.toml) for centralized dependency and plugin version control.

- Known Limitations
API Rate Limiting: The Jikan API has strict rate limits. Rapidly refreshing the list may result in a 429 Too Many Requests error, which is logged but not yet handled with a "Try Again" UI button.

Search Functionality: Currently, the app displays the "Top Anime" list only. A manual search feature to query the Jikan database is not yet implemented.

Detail Depth: While the main list is comprehensive, the details screen is limited to basic info and does not currently support deep-linking to the official MyAnimeList pages.

Schema Migration: Currently, the Room database is set to destructive migration. Changes to the AnimeEntity will clear the local cache rather than migrating data (intended for initial development speed).
