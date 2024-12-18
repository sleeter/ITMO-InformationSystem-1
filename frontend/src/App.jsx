import React from 'react';
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from "./pages/Home.jsx";
import ProtectedRoute from "./routes/ProtectedRoute.jsx";
import AdminRequestTable from "./tables/Admin.jsx";

function App() {

  return (
      <Router>
          <div>
              {/* Маршруты */}
              <Routes>
                  <Route path="/auth/login" element={<Login />} />
                  <Route path="/auth/register" element={<Register />} />
                  <Route element={<ProtectedRoute />}>
                      <Route path="/home" element={<Home />} />
                      <Route path="/home/admin" element={<AdminRequestTable />} />
                  </Route>
                  <Route path="/" element={<Login />} /> {/* Страница по умолчанию */}
              </Routes>
          </div>
      </Router>
  )
}

export default App
