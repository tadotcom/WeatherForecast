# WeatherForecast App

## デモ動画
アプリの動作イメージです。<br>
画面遷移と、状況に応じたエラーハンドリングをご確認ください。

https://github.com/user-attachments/assets/b70a2350-4dca-461b-a8c7-8737c0cf2a3f

## スクリーンショット

| ホーム画面 | 天気画面 | エラー画面 |
|-----------|---------|-----------|
| ![ホーム画面](https://github.com/user-attachments/assets/e0ff91fb-bd08-4591-a807-973186f25ed7) | ![天気画面](https://github.com/user-attachments/assets/27bbe790-7562-4fe8-b2fa-dc4184fbfa9c) | ![エラー画面](https://github.com/user-attachments/assets/cbf86e74-9f04-4487-804e-d770bab05e85) |

## 概要
都市名から天気情報を検索・表示するAndroidアプリケーションです。
「実務での運用」を想定し、モダンなアーキテクチャ（MVVM + Clean Architecture）と、保守性の高い技術スタック（Jetpack Compose, Hilt）を採用して構築しました。

## 主な機能
- **都市別天気予報:**
  - 現在地から探す、東京、兵庫、大分、北海道の天気をリスト表示。
- **詳細天気表示:**
  - ファーストビューで「現在の天気」を強調表示。
  - 3時間ごとの週間予報をリスト表示。
- **昼夜の自動判定:**
  - APIの時刻データに基づき、昼（Day）と夜（Night）のアイコンを適切に出し分け。
- **オフラインキャッシュ**
  - Roomデータベースを使用し、取得した天気データをキャッシュ。
  - **キャッシュ戦略:** 同日中の再アクセス時はAPIを叩かずキャッシュを表示し、通信量を削減。
  - **自動クリーニング:** DB肥大化を防ぐため、当日以外の古いキャッシュを自動削除するロジックを実装。
- **現在地取得**
  - 端末の位置情報サービスを利用し、現在地の天気を検索可能。


## ディレクトリ構成
```
app/src/main/java/com/miyazaki/weatherforecast/
├── data/              # データ層
│   ├── local/        # Room
│   ├── remote/       # Retrofit
│   └── repository/   # Repository実装
├── domain/            # ドメイン層
│   ├── model/        # DomainModel
│   ├── repository/   # Repository IF
│   └── usecase/      # UseCase
├── presentation/      # プレゼンテーション層
│   ├── ui/           # Screen, ViewModel
│   ├── navigation/   # Navigation
│   └── theme/        # Theme
└── di/                # Hilt Module
```


## セットアップ

### 1. API Keyの取得

[OpenWeatherMap](https://openweathermap.org/api)でAPIキーを取得してください。

### 2. API Keyの設定

プロジェクト直下の `local.properties` に以下を追加:
```properties
WEATHER_API_KEY=あなたのAPIキー
```

### 3. ビルド
```bash
./gradlew assembleDebug
```

## 技術スタック
- **Language:** Kotlin
- **UI:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM + Clean Architecture (Presentation / Domain / Data)
- **DI:** Hilt
- **Network:** Retrofit2, OkHttp3
- **JSON Parser:** Moshi (KotlinJsonAdapterFactory)
- **Database:** Room
- **Async:** Coroutines, Flow
- **Image Loading:** Coil
- **Testing:** JUnit4, Mockk, Kotlinx-coroutines-test

## 工夫した点

### 1. 設計と保守性
- **Clean Architecture:**
  - ビジネスロジック（Domain）と技術的詳細（Data/Presentation）を分離し、テスト容易性を確保しました。
- **JSONライブラリの統一:**
  - RoomのTypeConverterでGsonを使用していた箇所をMoshiにリファクタリングし、アプリサイズの削減と依存関係の整理を行いました。
- **単体テスト (Unit Testing):**
  - `Mockk` と `TestDispatcher` を使用し、ViewModelの状態遷移（Loading -> Success/Error）を検証するテストコードを実装済みです。

### 2. UI/UXの改善
- **視認性の向上:**
  - 仕様書の「リスト表示」という要件を守りつつ、最も重要な「現在の天気」をヘッダーとして大きく表示するレイアウトを採用しました。
- **エラーハンドリング:**
  - ユーザーが状況を理解できるよう、HTTPステータスや通信状態に応じて「ネット接続を確認してください」「サーバーエラー」などの具体的な日本語メッセージを出し分けています。

### 3. パフォーマンス
- **キャッシュ運用:**
  - `WeatherRepository` にて「API取得 → DB保存 → 古いキャッシュ削除」の一連の流れを実装し、アプリの長期利用でもストレージを圧迫しない設計にしました。

## 動作環境
- Android Studio Otter 2 Feature Drop | 2025.2.2
- Target SDK: 36
- Min SDK: 24
