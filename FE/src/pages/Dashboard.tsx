import React from 'react';
import {
  Grid,
  Paper,
  Typography,
  Box,
  Card,
  CardContent,
} from '@mui/material';
import {
  Timeline,
  TimelineItem,
  TimelineSeparator,
  TimelineConnector,
  TimelineContent,
  TimelineDot,
} from '@mui/lab';
import {
  ShoppingCart as OrderIcon,
  Speed as SpeedIcon,
  Error as ErrorIcon,
  CheckCircle as SuccessIcon,
} from '@mui/icons-material';

const Dashboard: React.FC = () => {
  // Mock data - replace with actual API calls
  const stats = {
    totalOrders: 15000,
    averageLatency: 150,
    errorRate: 0.5,
    successRate: 99.5,
  };

  const recentEvents = [
    {
      time: '10:45 AM',
      type: 'order',
      message: 'New order #12345 received',
      status: 'success',
    },
    {
      time: '10:44 AM',
      type: 'error',
      message: 'Failed to process order #12344',
      status: 'error',
    },
    {
      time: '10:43 AM',
      type: 'performance',
      message: 'System latency increased to 180ms',
      status: 'warning',
    },
  ];

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>

      <Grid container spacing={3}>
        {/* Stats Cards */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <OrderIcon color="primary" sx={{ mr: 1 }} />
                <Typography variant="h6">Total Orders</Typography>
              </Box>
              <Typography variant="h4">{stats.totalOrders}</Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <SpeedIcon color="primary" sx={{ mr: 1 }} />
                <Typography variant="h6">Avg Latency</Typography>
              </Box>
              <Typography variant="h4">{stats.averageLatency}ms</Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <ErrorIcon color="error" sx={{ mr: 1 }} />
                <Typography variant="h6">Error Rate</Typography>
              </Box>
              <Typography variant="h4">{stats.errorRate}%</Typography>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center">
                <SuccessIcon color="success" sx={{ mr: 1 }} />
                <Typography variant="h6">Success Rate</Typography>
              </Box>
              <Typography variant="h4">{stats.successRate}%</Typography>
            </CardContent>
          </Card>
        </Grid>

        {/* Recent Events Timeline */}
        <Grid item xs={12}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Recent Events
            </Typography>
            <Timeline>
              {recentEvents.map((event, index) => (
                <TimelineItem key={index}>
                  <TimelineSeparator>
                    <TimelineDot
                      color={
                        event.status === 'success'
                          ? 'success'
                          : event.status === 'error'
                          ? 'error'
                          : 'warning'
                      }
                    />
                    {index < recentEvents.length - 1 && <TimelineConnector />}
                  </TimelineSeparator>
                  <TimelineContent>
                    <Typography variant="subtitle2">{event.time}</Typography>
                    <Typography>{event.message}</Typography>
                  </TimelineContent>
                </TimelineItem>
              ))}
            </Timeline>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard; 