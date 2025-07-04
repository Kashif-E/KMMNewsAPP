# 📝 KMMNewsAPP Implementation TODO

This file tracks the step-by-step implementation of the Twine-like News Reader App using KMP, SKIE, Room, Koin, Compose, and SwiftUI. Each major task is listed as a checklist. When a feature is completed, it will be marked as **done** and the next feature will be started.

---

## 🚦 Implementation Plan

### 1. Project Scaffolding & Core Setup
- [ ] Scaffold modular directory structure (core, feature/headlines, etc.)
- [ ] Implement core/network (Ktor setup, interceptors, ApiResult)
- [ ] Implement core/database (Room KMP config, base DAOs, migrations)
- [ ] Implement core/utils (Result wrappers, extensions)
- [ ] Implement core/di (Koin global modules)
- [ ] Add SKIE plugin and base configuration

### 2. Feature: Headlines (End-to-End)
- [ ] Scaffold feature/headlines module (data, domain, presentation, di)
- [ ] Implement headlines/data (API, DTOs, DAOs, mappers, repository impl)
- [ ] Implement headlines/domain (repository interface, use case, models)
- [ ] Implement headlines/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement headlines/di (feature DI module)
- [ ] Compose/SwiftUI UI for headlines (list, filters, pull-to-refresh)
- [ ] Room caching for headlines
- [ ] Multiplatform tests for headlines (use case, reducer, repository, DAO)
- [ ] Add SKIE annotations for Swift interop

### 3. Feature: Bookmarks
- [ ] Scaffold feature/bookmarks module
- [ ] Implement bookmarks/data (Room DAO, repository impl)
- [ ] Implement bookmarks/domain (repository interface, use case, models)
- [ ] Implement bookmarks/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement bookmarks/di (feature DI module)
- [ ] Compose/SwiftUI UI for bookmarks
- [ ] Multiplatform tests for bookmarks
- [ ] Add SKIE annotations for Swift interop

### 4. Feature: Search
- [ ] Scaffold feature/search module
- [ ] Implement search/data (API, repository impl)
- [ ] Implement search/domain (repository interface, use case, models)
- [ ] Implement search/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement search/di (feature DI module)
- [ ] Compose/SwiftUI UI for search
- [ ] Multiplatform tests for search
- [ ] Add SKIE annotations for Swift interop

### 5. Feature: Categories
- [ ] Scaffold feature/categories module
- [ ] Implement categories/data (constants, repository impl)
- [ ] Implement categories/domain (repository interface, use case, models)
- [ ] Implement categories/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement categories/di (feature DI module)
- [ ] Compose/SwiftUI UI for categories (tabbed UI)
- [ ] Multiplatform tests for categories
- [ ] Add SKIE annotations for Swift interop

### 6. Feature: Sources
- [ ] Scaffold feature/sources module
- [ ] Implement sources/data (API, repository impl)
- [ ] Implement sources/domain (repository interface, use case, models)
- [ ] Implement sources/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement sources/di (feature DI module)
- [ ] Compose/SwiftUI UI for sources
- [ ] Multiplatform tests for sources
- [ ] Add SKIE annotations for Swift interop

### 7. Feature: Everything (Unified Feed)
- [ ] Scaffold feature/everything module
- [ ] Implement everything/data (API, repository impl)
- [ ] Implement everything/domain (repository interface, use case, models)
- [ ] Implement everything/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement everything/di (feature DI module)
- [ ] Compose/SwiftUI UI for everything
- [ ] Multiplatform tests for everything
- [ ] Add SKIE annotations for Swift interop

### 8. Feature: Settings
- [ ] Scaffold feature/settings module
- [ ] Implement settings/data (Room DAO, repository impl)
- [ ] Implement settings/domain (repository interface, use case, models)
- [ ] Implement settings/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement settings/di (feature DI module)
- [ ] Compose/SwiftUI UI for settings
- [ ] Multiplatform tests for settings
- [ ] Add SKIE annotations for Swift interop

### 9. Feature: Onboarding
- [ ] Scaffold feature/onboarding module
- [ ] Implement onboarding/data (Room DAO, repository impl)
- [ ] Implement onboarding/domain (repository interface, use case, models)
- [ ] Implement onboarding/presentation (Intent, State, Effect, Reducer, ViewModel)
- [ ] Implement onboarding/di (feature DI module)
- [ ] Compose/SwiftUI UI for onboarding
- [ ] Multiplatform tests for onboarding
- [ ] Add SKIE annotations for Swift interop

### 10. Offline Mode
- [ ] Implement offline caching for all features (Room)
- [ ] Ensure all features work offline

### 11. Testing & Documentation
- [ ] Add multiplatform test coverage for all features
- [ ] Add preview states for Compose/SwiftUI
- [ ] Write comprehensive README (setup, API key, build, contribution)

---

## 🔄 When a feature is completed, mark it as `[x]` and proceed to the next.
