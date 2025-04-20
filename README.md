# IntelliJence – AI Chat App

🧠 **IntelliJence** is a Java-based Spring Boot web application that lets users interact with AI models like ChatGPT via a custom-built chat interface. It uses an OpenAI-compatible API and stores messages using a PostgreSQL database.

## 🔧 Features

- Send and receive AI-generated messages
- Stores message history in a database
- Chat interface integrated into a web front-end
- Modular design using Spring Boot and Lombok
- Connects to models via API (e.g. OpenRouter)

## 🛠️ Technologies Used

- Java 21
- Spring Boot
- PostgreSQL
- Lombok
- OkHttp (for HTTP requests)
- Jackson (for JSON processing)
- Thymeleaf / HTML (for front-end)

## 🚀 How to Run

1. Clone the project
2. Setup PostgreSQL DB (check `application.properties`)
3. Add your API key to the `models` table
4. Run the Spring Boot app
5. Open your browser at `http://localhost:8080`

## 📦 Example Model Entry

```sql
INSERT INTO MODELS(NAME, VERSION, URL, API_KEY, TYPE)
VALUES(
  'DeepSeek',
  'deepseek/deepseek-chat',
  'https://openrouter.ai/api/v1/chat/completions',
  'your-api-key-here',
  'http'
);
