import React from 'react';
import {
  Box,
  Typography,
  Paper,
  Grid,
  Card,
  CardContent,
} from '@mui/material';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  BarChart,
  Bar,
} from 'recharts';

const Analytics: React.FC = () => {
  // Mock data - replace with actual API calls
  const throughputData = [
    { time: '00:00', orders: 120 },
    { time: '01:00', orders: 150 },
    { time: '02:00', orders: 180 },
    { time: '03:00', orders: 200 },
    { time: '04:00', orders: 220 },
    { time: '05:00', orders: 250 },
  ];

  const latencyData = [
    { time: '00:00', latency: 150 },
    { time: '01:00', latency: 145 },
    { time: '02:00', latency: 160 },
    { time: '03:00', latency: 155 },
    { time: '04:00', latency: 140 },
    { time: '05:00', latency: 150 },
  ];

  const errorDistribution = [
    { type: 'Validation', count: 15 },
    { type: 'Timeout', count: 8 },
    { type: 'Database', count: 5 },
    { type: 'Network', count: 3 },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Analytics
      </Typography>

      <Grid container spacing={3}>
        {/* Throughput Chart */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Orders Throughput
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={throughputData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="orders"
                  stroke="#8884d8"
                  name="Orders per Hour"
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Latency Chart */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Average Latency
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart data={latencyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="time" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line
                  type="monotone"
                  dataKey="latency"
                  stroke="#82ca9d"
                  name="Latency (ms)"
                />
              </LineChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Error Distribution */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Error Distribution
            </Typography>
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={errorDistribution}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="type" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="count" fill="#ff7300" name="Error Count" />
              </BarChart>
            </ResponsiveContainer>
          </Paper>
        </Grid>

        {/* Performance Metrics */}
        <Grid item xs={12} md={6}>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <Card>
                <CardContent>
                  <Typography color="textSecondary" gutterBottom>
                    Peak Throughput
                  </Typography>
                  <Typography variant="h4">250 orders/hr</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6}>
              <Card>
                <CardContent>
                  <Typography color="textSecondary" gutterBottom>
                    Avg Latency
                  </Typography>
                  <Typography variant="h4">150ms</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6}>
              <Card>
                <CardContent>
                  <Typography color="textSecondary" gutterBottom>
                    Error Rate
                  </Typography>
                  <Typography variant="h4">0.5%</Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={6}>
              <Card>
                <CardContent>
                  <Typography color="textSecondary" gutterBottom>
                    Success Rate
                  </Typography>
                  <Typography variant="h4">99.5%</Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Analytics; 