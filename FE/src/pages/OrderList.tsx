import React, { useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  TextField,
  Button,
  Chip,
} from '@mui/material';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import { Search as SearchIcon } from '@mui/icons-material';

interface Order {
  id: string;
  customerId: string;
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED';
  amount: number;
  createdAt: string;
  updatedAt: string;
}

const OrderList: React.FC = () => {
  const [searchQuery, setSearchQuery] = useState('');

  // Mock data - replace with actual API calls
  const orders: Order[] = [
    {
      id: '1',
      customerId: 'CUST001',
      status: 'COMPLETED',
      amount: 150.00,
      createdAt: '2024-03-20T10:00:00Z',
      updatedAt: '2024-03-20T10:00:05Z',
    },
    {
      id: '2',
      customerId: 'CUST002',
      status: 'PROCESSING',
      amount: 75.50,
      createdAt: '2024-03-20T10:01:00Z',
      updatedAt: '2024-03-20T10:01:02Z',
    },
    {
      id: '3',
      customerId: 'CUST003',
      status: 'FAILED',
      amount: 200.00,
      createdAt: '2024-03-20T10:02:00Z',
      updatedAt: '2024-03-20T10:02:10Z',
    },
  ];

  const columns: GridColDef[] = [
    { field: 'id', headerName: 'Order ID', width: 130 },
    { field: 'customerId', headerName: 'Customer ID', width: 130 },
    {
      field: 'status',
      headerName: 'Status',
      width: 130,
      renderCell: (params) => {
        const statusColors: Record<Order['status'], 'success' | 'error' | 'warning' | 'info'> = {
          PENDING: 'warning',
          PROCESSING: 'info',
          COMPLETED: 'success',
          FAILED: 'error',
        };
        return (
          <Chip
            label={params.value}
            color={statusColors[params.value as Order['status']]}
            size="small"
          />
        );
      },
    },
    {
      field: 'amount',
      headerName: 'Amount',
      width: 130,
      valueFormatter: (params) => `$${params.value.toFixed(2)}`,
    },
    {
      field: 'createdAt',
      headerName: 'Created At',
      width: 180,
      valueFormatter: (params) =>
        new Date(params.value as string).toLocaleString(),
    },
    {
      field: 'updatedAt',
      headerName: 'Updated At',
      width: 180,
      valueFormatter: (params) =>
        new Date(params.value as string).toLocaleString(),
    },
  ];

  const filteredOrders = orders.filter(
    (order) =>
      order.id.toLowerCase().includes(searchQuery.toLowerCase()) ||
      order.customerId.toLowerCase().includes(searchQuery.toLowerCase())
  );

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Orders
      </Typography>

      <Paper sx={{ p: 2, mb: 2 }}>
        <Box display="flex" gap={2} alignItems="center">
          <TextField
            label="Search Orders"
            variant="outlined"
            size="small"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            sx={{ flexGrow: 1 }}
          />
          <Button
            variant="contained"
            startIcon={<SearchIcon />}
            onClick={() => {
              // Implement search functionality
              console.log('Searching for:', searchQuery);
            }}
          >
            Search
          </Button>
        </Box>
      </Paper>

      <Paper sx={{ height: 600, width: '100%' }}>
        <DataGrid
          rows={filteredOrders}
          columns={columns}
          initialState={{
            pagination: {
              paginationModel: { pageSize: 10, page: 0 },
            },
          }}
          pageSizeOptions={[10, 25, 50]}
          checkboxSelection
          disableRowSelectionOnClick
        />
      </Paper>
    </Box>
  );
};

export default OrderList; 