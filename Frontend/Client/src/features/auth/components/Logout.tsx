import React from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { toast } from "react-toastify";

const Logout: React.FC = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    const accessToken = localStorage.getItem("accessToken");
    if (!accessToken) {
      console.warn("No access token found, clearing session locally.");
      localStorage.clear();
      navigate("/login");
      return;
    }
  
    try {
      toast.info("Logging out...");
      const keycloakLogoutUrl = `http://localhost:8080/realms/CtrlEdu/protocol/openid-connect/logout`;
      await axios.get(keycloakLogoutUrl, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      });
  
      localStorage.clear();
      navigate("/login");
    } catch (error) {
      console.error("Keycloak logout failed", error);
      // Optionally clear local session even if Keycloak logout fails
      localStorage.clear();
      navigate("/login");
    }
  };

  return (
    <button
      onClick={handleLogout}
      className="bg-red-500 hover:bg-red-700 text-white py-2 px-4 rounded"
    >
      Logout
    </button>
  );
};

export default Logout;
