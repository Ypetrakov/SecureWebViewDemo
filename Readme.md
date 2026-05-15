# Інструкція
Ви можете завантажити та встановити останню версію додатка прямо з файлу:
[petrakov.webview.apk](petrakov.webview.apk) 

> Minimum Android version: 7.0
> Target SDK: 37

### Requirements
- Android Studio Panda
- Kotlin 2.3.21
- Gradle 9.5.1
- Min SDK 24

### Run project
```bash
git clone https://github.com/Ypetrakov/SecureWebViewDemo.git
cd SecureWebViewDemo
chmod +x gradlew
./gradlew installDebug
```

### Open with deeplink
```bash
adb shell am start \
-a android.intent.action.VIEW \
-d 'myapp://game?url=aHR0cHM6Ly9uZXdzLnljb21iaW5hdG9yLmNvbS8=\&title=Test'
```

# Архітектура та структура
Додаток побудований з clean architecture. Використовувався Koin для DI

У додатку реалізовані наступні екрани:
- Головний екран
- Екран WebView
- Додатково CustomTabs

# Опис Екранів
Головний екран: 
- Є можливість введення посилання, якщо посилання не писати, то буде використане дефолтне посилання, яке закодоване в base64.
- Можна очистити кеш webview (перевірив за допомогою https://www.refreshyourcache.com/en/cache-test/)
- Можна симулювати відсутність доступу webview(світч зберігає стан між запусками, для цього створений окремий клас [ContentGatePreferences.kt](app/src/main/java/com/yurrii/petrakov/swvd/data/local/prefs/ContentGatePreferences.kt))
- Є кнопка для симуляції відправки івента в аналітику та лог усіх івентів які відбувалися до цього
- Є блок з діплінком, де можна відкрити додаток з діплінком або скопіювати його.

Екран WebView:
- Є три стани: Loading(Поки webview завантажується), Success(Webview успішно відкрилося), ContentGate(щось пішло не так)

# Acceptance Checklist:
1. HTTPS-завантаження - коректно завантажуються, відображається WebView, Success state
2. HTTP → HTTPS redirect - При завантаженні посилання з HTTP схемою, якщо воно відкривається в webview, схема буде замінена на https.
3. Зовнішній домен - домени яких немає в allowList відкриваються в окремому вікні в Custom Tabs
4. Deep link (cold start) - додаток запускається коректно, одразу відкривається webview, Title відображається в toolbar, усі чіпи про те що відкрито в webview присутні. Додаток працює як потрібно.

Команда по якій я відкривав - https://news.ycombinator.com/ - WebView

adb shell am start \
-a android.intent.action.VIEW \
-d 'myapp://game?url=aHR0cHM6Ly9uZXdzLnljb21iaW5hdG9yLmNvbS8=\&title=Test'

---
Комана по якій я відкривав https://tiger.oneappssite.website/ - CustomTabs

adb shell am start \
-a android.intent.action.VIEW \
-d 'myapp://game?url=aHR0cHM6Ly90aWdlci5vbmVhcHBzc2l0ZS53ZWJzaXRlLw=\&title=Test'

Також можна відкрити по кнопці з додатку

6. Повторне відкриття WebView - Відкрив webview, перейшов назад, усе відображається коректно

6. Відсутність інтернету - Вимкнув інтернет, відкрив webview, перекинуло в Content Gate, описало проблему

7. Background / Foreground - Відкрив WebView, згорнув додаток, повернувся - усе працює коректно.

Усі посилання які обфусковані за допомогою base64, для коректної роботи deeplink посилання всередині теж потрібно закодувати

WebView відкривається тільки для сайтів, які знаходяться в AllowHosts, в інших випадках посилання відкриється в CustomTabs.

# Аналітика
Для симуляції івентів створена [AnalyticsTrackerImpl.kt](app/src/main/java/com/yurrii/petrakov/swvd/data/analytics/AnalyticsTrackerImpl.kt), він симулює логування подій, також усі події відображаються в Logcat з тегом [APP_ANALYTICS] та зберігаєтсья локально в бд Room.
В додатку є кнопка для ручної відправки івента, а також різні автоматичні івенти при різних сценаріях

# Deep Link Security
В [UrlHandler.kt](app/src/main/java/com/yurrii/petrakov/swvd/domain/util/UrlHandler.kt) реалізовано методи для перевірки deep link.
- Дозволені лише конкретні scheme: myapp
  - fdsqwoxss
- Дозволений лише конкретний host:
  - game
- Обов’язкова наявність параметрів:
  - url
  - title
- URL передається у Base64 та декодується перед обробкою.
  Після декодування перевіряється:
  наявність http або https
  відповідність allowlist

Для запобігання відкриттю сторонніх ресурсів використовується allowlist доменів.
URL вважається валідним лише якщо:
- його host збігається з одним із дозволених доменів;
- URL починається з дозволеного базового шляху.

AllowHosts:
- https://www.refreshyourcache.com/en/cache-test/
- https://news.ycombinator.com/
- https://example.com

# Захист default URL
Реалізована базова обфускація за допомогою Base64, що дає мінімальний захисти від прямого доступу до рядку

# Custom Tab
Усі посилання відкриті, які не належать allowhost відкриваються в Custom Tab.

Коли натискаєш на хрестик, додаток закривається повністю, це досягнуто за допомогою методу finishAndRemoveTask() коли додаток після Custom Tab переходить к стан OnResume(попередньо перед відкриття Custom Tab змінюється флаг кастом там відкрився для коректної роботи)

Також реалізований окремий діп лінк який обробляє кнопку з політики і відкривається екран з webview

Усі події логуються

# Можливі покращення
- перевірка доступності http -> htpps редіректу
- Ускладнити Захист default URL 