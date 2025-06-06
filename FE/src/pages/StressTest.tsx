import React, { useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Grid,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  CircularProgress,
  Alert,
  SelectChangeEvent,
} from '@mui/material';
import { PlayArrow as StartIcon, Stop as StopIcon } from '@mui/icons-material';

interface TestConfig {
  numOrders: number;
  batchSize: number;
  delayBetweenBatches: number;
  orderType: 'SIMPLE' | 'COMPLEX';
}

const StressTest: React.FC = () => {
  const [config, setConfig] = useState<TestConfig>({
    numOrders: 1000,
    batchSize: 100,
    delayBetweenBatches: 1000,
    orderType: 'SIMPLE',
  });

  const [isRunning, setIsRunning] = useState(false);
  const [results, setResults] = useState<{
    success: boolean;
    message: string;
    metrics?: {
      totalTime: number;
      avgLatency: number;
      successRate: number;
      errorRate: number;
    };
  } | null>(null);

  const handleTextChange = (field: keyof Omit<TestConfig, 'orderType'>) => (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    setConfig({
      ...config,
      [field]: Number(event.target.value),
    });
  };

  const handleSelectChange = (event: SelectChangeEvent) => {
    setConfig({
      ...config,
      orderType: event.target.value as 'SIMPLE' | 'COMPLEX',
    });
  };

  const handleStartTest = async () => {
    setIsRunning(true);
    setResults(null);

    try {
      // Mock API call - replace with actual implementation
      await new Promise((resolve) => setTimeout(resolve, 5000));

      setResults({
        success: true,
        message: 'Stress test completed successfully',
        metrics: {
          totalTime: 45.2,
          avgLatency: 150,
          successRate: 99.5,
          errorRate: 0.5,
        },
      });
    } catch (error) {
      setResults({
        success: false,
        message: 'Stress test failed: ' + (error as Error).message,
      });
    } finally {
      setIsRunning(false);
    }
  };

  const handleStopTest = () => {
    setIsRunning(false);
    setResults({
      success: false,
      message: 'Stress test stopped by user',
    });
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Stress Test
      </Typography>

      <Grid container spacing={3}>
        {/* Configuration */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Test Configuration
            </Typography>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Number of Orders"
                  type="number"
                  value={config.numOrders}
                  onChange={handleTextChange('numOrders')}
                  disabled={isRunning}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Batch Size"
                  type="number"
                  value={config.batchSize}
                  onChange={handleTextChange('batchSize')}
                  disabled={isRunning}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Delay Between Batches (ms)"
                  type="number"
                  value={config.delayBetweenBatches}
                  onChange={handleTextChange('delayBetweenBatches')}
                  disabled={isRunning}
                />
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth disabled={isRunning}>
                  <InputLabel>Order Type</InputLabel>
                  <Select
                    value={config.orderType}
                    label="Order Type"
                    onChange={handleSelectChange}
                  >
                    <MenuItem value="SIMPLE">Simple</MenuItem>
                    <MenuItem value="COMPLEX">Complex</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
          </Paper>
        </Grid>

        {/* Controls and Results */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Test Controls
            </Typography>
            <Box display="flex" gap={2} mb={3}>
              <Button
                variant="contained"
                color="primary"
                startIcon={isRunning ? <CircularProgress size={20} /> : <StartIcon />}
                onClick={handleStartTest}
                disabled={isRunning}
              >
                Start Test
              </Button>
              <Button
                variant="contained"
                color="error"
                startIcon={<StopIcon />}
                onClick={handleStopTest}
                disabled={!isRunning}
              >
                Stop Test
              </Button>
            </Box>

            {results && (
              <Box>
                <Alert
                  severity={results.success ? 'success' : 'error'}
                  sx={{ mb: 2 }}
                >
                  {results.message}
                </Alert>

                {results.metrics && (
                  <Grid container spacing={2}>
                    <Grid item xs={6}>
                      <Typography color="textSecondary">Total Time</Typography>
                      <Typography variant="h6">
                        {results.metrics.totalTime}s
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      <Typography color="textSecondary">Avg Latency</Typography>
                      <Typography variant="h6">
                        {results.metrics.avgLatency}ms
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      <Typography color="textSecondary">Success Rate</Typography>
                      <Typography variant="h6">
                        {results.metrics.successRate}%
                      </Typography>
                    </Grid>
                    <Grid item xs={6}>
                      <Typography color="textSecondary">Error Rate</Typography>
                      <Typography variant="h6">
                        {results.metrics.errorRate}%
                      </Typography>
                    </Grid>
                  </Grid>
                )}
              </Box>
            )}
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default StressTest; 