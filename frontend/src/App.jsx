import './App.css'
import { Link, Navigate, Route, Routes, useNavigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import SolarWatchPage from "./pages/SolarWatchPage.jsx";
import NotFoundPage from "./pages/NotFoundPage.jsx";
import ProtectedRoute from './components/ProtectedRoute.jsx';
import { useAuth } from './context/AuthContext.jsx';


function App() {
  const { token, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate("/login", { replace: true });
  }

  return (
    <div>
      <nav>
        {token ? (<>
          <button onClick={handleLogout}>Logout</button>
          <Link to={"/solar-watch"}>SolarWatch</Link>
          </>
        ) : <>
          <Link to={"/registration"}>Register</Link>
          <Link to={"/login"}>Login</Link>
        </>
        }
        
      </nav>
      <Routes>
        <Route path={"/"} element={<Navigate to="/login" replace />} />
        <Route path={"/login"} element={<LoginPage />} />
        <Route path={"/registration"} element={<RegisterPage />} />
        <Route path={"/solar-watch"} element={<ProtectedRoute><SolarWatchPage /></ProtectedRoute>} />
        <Route path={"*"} element={<NotFoundPage />} />
      </Routes>
    </div>
  )
}

export default App
