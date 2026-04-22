# ComicVineAppSample

ComicVineAppSample is a sample Android application that explores modern Android development around the Comic Vine API.

The project focuses on a small but complete feature set:
- character listing with pagination
- character search
- character detail
- local favorites
- light, dark, and automatic theme modes

The codebase is organized as a multi-module project and follows a clean separation between UI, domain, and data concerns.

## Table of Contents

- Overview
- Features
- Architecture
- Modules
- Tech Stack
- Data Flow
- Project Setup
- Quality Checks

## Overview

This app fetches Comic Vine characters from the network, persists them locally with Room, and exposes them to the UI using Kotlin Flow.

The current implementation uses an offline-first approach for the main flows:
- the UI observes Room as the source of truth
- the repository refreshes data from the API when needed
- detail data enriches the local cache when long descriptions are missing
- favorites are updated locally and reflected automatically through Room emissions

## Features

- Browse Comic Vine characters with paginated loading
- Search characters by name from the current cached list
- Open a character detail screen with long description fallback
- Save and remove favorites locally
- Reactive favorites screen backed by Room Flow
- Theme switching with light, dark, and follow-system modes
- Android Splash Screen API integration

## Architecture

The app is structured around:

- `MVVM` for presentation logic
- `Clean Architecture` style boundaries between UI, domain, and data
- `Single Activity + Navigation Component`
- `Offline-first repository flows` for list, detail, and favorites

At a high level:

1. The UI collects `StateFlow` from the `ViewModel`.
2. The `ViewModel` coordinates use cases and maps domain models into display models.
3. The domain layer defines business models, repositories, and use cases.
4. The data layer handles local and remote data sources plus repository implementations.
5. Room acts as the local source of truth, while Retrofit refreshes the cache when necessary.

## Modules

### `:app`

Android-specific layer containing:
- Activities and Fragments
- ViewModels
- UI display models and mappers
- Room database implementation
- Retrofit services
- Hilt modules

### `:domain`

Pure business layer containing:
- domain models
- repository contracts
- use cases
- failure abstractions

### `:data`

Implementation layer containing:
- repository implementations
- datasource contracts
- data mappers
- data-specific models

## Tech Stack

- `Kotlin`
- `Coroutines` and `Flow`
- `ViewModel`
- `Navigation Component` with Safe Args
- `Room`
- `Retrofit`
- `OkHttp`
- `Kotlinx Serialization`
- `Hilt`
- `Arrow Either`
- `DataStore`
- `Glide`
- `Lottie`
- `SwipeRevealLayout`
- `Detekt`

## Data Flow

### Characters list

- `MainViewModel` observes characters through a use case
- the repository exposes Room data as `Flow`
- on first collection, the repository refreshes from the API
- paginated requests update Room and the UI reacts automatically

### Character detail

- `DetailViewModel` observes one character from Room
- if the cached entry does not have the long description, the repository fetches the detail endpoint
- the local entity is updated and the UI refreshes automatically

### Favorites

- favorites are stored locally in Room
- the favorites screen observes `Flow<List<...>>`
- removing a favorite updates the database and the screen reacts without manual reloads

## Project Setup

### Requirements

- Android Studio with recent Android Gradle Plugin support
- JDK 17

### API Key

This project requires a Comic Vine API key.

Create or update `local.properties` in the project root with:

```properties
COMIC_VINE_API_KEY=<your_api_key>
```

You can get an API key from:

- https://comicvine.gamespot.com/api/

### Build

To compile the app:

```bash
./gradlew :app:compileDebugKotlin
```

## Quality Checks

Run Detekt:

```bash
./gradlew detekt
```

The project currently compiles and passes Detekt with the checked-in changes.
