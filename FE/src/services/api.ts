import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export interface Order {
  id: string;
  customerId: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
  amount: number;
  createdAt: string;
  updatedAt: string;
}

export interface TestConfig {
  numOrders: number;
  batchSize: number;
  delayBetweenBatches: number;
  orderType: 'SIMPLE' | 'COMPLEX';
}

export interface TestResults {
  success: boolean;
  message: string;
  metrics?: {
    totalTime: number;
    avgLatency: number;
    successRate: number;
    errorRate: number;
  };
}

export interface AnalyticsData {
  throughput: Array<{ time: string; orders: number }>;
  latency: Array<{ time: string; latency: number }>;
  errorDistribution: Array<{ type: string; count: number }>;
  metrics: {
    peakThroughput: number;
    avgLatency: number;
    errorRate: number;
    successRate: number;
  };
}

export const orderService = {
  // Get all orders with pagination and filtering
  getOrders: async (page = 0, size = 10, search?: string) => {
    const params = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      ...(search && { search }),
    });
    const response = await api.get(`/api/orders?${params}`);
    return response.data;
  },

  // Get a single order by ID
  getOrder: async (id: string) => {
    const response = await api.get(`/api/orders/${id}`);
    return response.data;
  },

  // Create a new order
  createOrder: async (order: Omit<Order, 'id' | 'createdAt' | 'updatedAt'>) => {
    const response = await api.post('/api/orders', order);
    return response.data;
  },
};

export const analyticsService = {
  // Get analytics data
  getAnalytics: async (): Promise<AnalyticsData> => {
    const response = await api.get('/api/analytics');
    return response.data;
  },

  // Get throughput data
  getThroughput: async () => {
    const response = await api.get('/api/analytics/throughput');
    return response.data;
  },

  // Get latency data
  getLatency: async () => {
    const response = await api.get('/api/analytics/latency');
    return response.data;
  },

  // Get error distribution
  getErrorDistribution: async () => {
    const response = await api.get('/api/analytics/errors');
    return response.data;
  },
};

export const stressTestService = {
  // Start a stress test
  startTest: async (config: TestConfig): Promise<TestResults> => {
    const response = await api.post('/api/stress-test', config);
    return response.data;
  },

  // Stop a running stress test
  stopTest: async (): Promise<void> => {
    await api.post('/api/stress-test/stop');
  },

  // Get test status
  getTestStatus: async (): Promise<{ isRunning: boolean; results?: TestResults }> => {
    const response = await api.get('/api/stress-test/status');
    return response.data;
  },
};

export default {
  orderService,
  analyticsService,
  stressTestService,
}; 