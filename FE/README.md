# Order Processing System Frontend

This is the frontend application for the high-performance order processing system. It provides a modern, responsive user interface for monitoring and managing orders, viewing analytics, and running stress tests.

## Features

- Real-time order monitoring and management
- Performance analytics with interactive charts
- Stress testing capabilities
- Responsive design for all devices
- Modern Material-UI components
- TypeScript for type safety

## Prerequisites

- Node.js 18 or later
- npm 9 or later

## Getting Started

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:
   ```bash
   npm start
   ```

3. Build for production:
   ```bash
   npm run build
   ```

## Development

The application is built with:
- React 18
- TypeScript
- Material-UI
- React Query for data fetching
- React Router for navigation
- Recharts for data visualization

### Project Structure

```
src/
  ├── components/     # Reusable UI components
  ├── pages/         # Page components
  ├── services/      # API services
  ├── types/         # TypeScript type definitions
  ├── App.tsx        # Main application component
  └── index.tsx      # Application entry point
```

### Environment Variables

Create a `.env` file in the root directory with the following variables:

```
REACT_APP_API_BASE_URL=http://localhost:8080
```

## Docker

The application can be built and run using Docker:

```bash
# Build the image
docker build -t order-processing-frontend .

# Run the container
docker run -p 80:80 order-processing-frontend
```

## Testing

Run the test suite:

```bash
npm test
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 