Portfolio Holdings App
A demo Android application showcasing stock portfolio holdings management with P&L calculations, built to demonstrate Clean Architecture and modern Android development practices.

📱 Features
✅ Display list of stock holdings
✅ Real-time Profit & Loss calculations
✅ Expandable/collapsible portfolio summary
✅ Pull-to-refresh functionality
✅ Offline-first architecture with caching
✅ Material Design 3 UI
✅ Smooth animations and transitions
🏗️ Architecture
This app follows Clean Architecture principles with MVVM pattern:

├── Presentation Layer (UI)
│   ├── Jetpack Compose UI
│   ├── ViewModels
│   └── Navigation
├── Domain Layer (Business Logic)
│   ├── Use Cases
│   ├── Domain Models
│   └── Calculator
└── Data Layer
    ├── Repository
    ├── Remote API (Retrofit)
    ├── Local Database (Room)
    └── In-Memory Cache
Architecture Benefits
Testable: Each layer can be tested independently
Maintainable: Clear separation of concerns
Scalable: Easy to extend with new features
Learning-focused: Demonstrates best practices for Android development
🛠️ Tech Stack
Language: Kotlin
UI: Jetpack Compose (Material3)
Architecture: Clean Architecture + MVVM
Dependency Injection: Hilt
Networking: Retrofit + OkHttp
Database: Room
Async: Kotlin Coroutines + Flow
Testing: JUnit, MockK, Turbine
📦 Project Structure
com.anurag.portfoliotask/
├── data/
│   ├── cache/           # In-memory cache
│   ├── local/           # Room database
│   │   ├── dao/
│   │   ├── db/
│   │   └── entities/
│   ├── remote/          # API services
│   │   ├── api/
│   │   └── model/
│   ├── mapper/          # Data transformations
│   └── repository/      # Data source management
├── domain/
│   ├── calculator/      # Business calculations
│   ├── model/           # Domain models
│   └── usecase/         # Use cases
├── ui/
│   ├── screens/         # Composable screens
│   ├── viewmodel/       # ViewModels
│   └── navigation/      # Navigation setup
└── di/                  # Dependency injection
🚀 Getting Started
Prerequisites
Android Studio Hedgehog | 2023.1.1 or later
Minimum SDK: 21
Target SDK: 34
JDK 17
Installation
Clone the repository
bash
git clone https://github.com/anurag0000/Anurag-Goswami-Portfolio-Task.git
cd Anurag-Goswami-Portfolio-Task
Open project in Android Studio
Sync Gradle files
Run the app on emulator or physical device
📊 API Endpoint
The app fetches holdings data from:

https://35dee773a9ec441e9f38d5fc249406ce.api.mockbin.io/
🧮 Calculations
Current Value = Σ(LTP × Quantity)
Total Investment = Σ(Average Price × Quantity)
Total P&L = Current Value - Total Investment
Today's P&L = Σ((Close - LTP) × Quantity)
🧪 Testing
Run unit tests:

bash
./gradlew test
Run instrumentation tests:

bash
./gradlew connectedAndroidTest
Test Coverage
✅ Domain layer (Calculator, Use Cases)
✅ Data layer (Repository)
✅ Presentation layer (ViewModels)

Holdings list view
Collapsed summary card
Expanded summary with detailed breakdown
Pull-to-refresh action
Error and loading states
🎯 Purpose
This is a demonstration project created to showcase:

Modern Android development practices
Clean Architecture implementation
MVVM pattern with Jetpack Compose
Comprehensive testing approach
Offline-first data management
Material Design 3 guidelines

🎨 Design Decisions
Offline-First Approach
Implemented 3-tier caching strategy:

In-Memory Cache: Fastest access for active session
Room Database: Persistent storage for offline use
Network API: Source of truth for fresh data
UI/UX
Material Design 3 components
Color-coded P&L (green for profit, red for loss)
Smooth animations using animateContentSize()
Pull-to-refresh for manual updates
Proper loading and error states
🔄 Data Flow
App Launch
    ↓
Check In-Memory Cache
    ↓ (if empty)
Check Room Database
    ↓ (if empty)
Fetch from API
    ↓
Update All Caches → Display UI
📋 Key Features Implementation
1. Clean Architecture
Separation of concerns across layers
No framework dependencies in domain layer
Unidirectional data flow
2. Dependency Injection (Hilt)
Constructor injection for testability
Scoped instances (@Singleton, @ViewModelScoped)
Module-based organization
3. Reactive Programming
StateFlow for state management
Coroutines for async operations
Flow operators for data transformation
4. Error Handling
Try-catch at repository level
State-based error propagation
User-friendly error messages with retry
🔐 SOLID Principles
S: Each class has single responsibility
O: Open for extension (new data sources)
L: Dependency injection enables substitution
I: Focused interfaces (DAO, API)
D: Depend on abstractions, not implementations
📈 Performance Optimizations
In-memory caching for instant access
LazyColumn for efficient list rendering
Keys in list items for targeted recomposition
Singleton network and database instances
🛠️ Build Configuration
Dependencies
See build.gradle.kts for complete list including:

Jetpack Compose BOM
Hilt for Dependency Injection
Retrofit for networking
Room for local storage
Testing libraries (JUnit, MockK, Turbine)
Build Variants
Debug: Development build with logging enabled
Release: Optimized build (not configured for production deployment)


