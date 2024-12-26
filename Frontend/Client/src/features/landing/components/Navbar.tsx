import { Menu, X } from "lucide-react";
import { useState } from "react";
import logo from "../../../assets/logo1.svg";
import { navItems } from "../../../constants";
import { Link } from "react-scroll";
import { useNavigate } from "react-router-dom";

type NavItem = {
  href: string;
  label: string;
};

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const [mobileDrawerOpen, setMobileDrawerOpen] = useState<boolean>(false);

  const toggleNavbar = () => {
    setMobileDrawerOpen(!mobileDrawerOpen);
  };

  return (
    <nav className="sticky top-0 z-50 py-3 backdrop-blur-lg border-b border-neutral-700/80 bg-white dark:bg-neutral-900 text-black dark:text-white">
      <div className="container px-4 mx-auto relative lg:text-sm">
        <div className="flex justify-between items-center">
          <div className="flex items-center flex-shrink-0">
            <img className="h-20 w-29 mr-4" src={logo} alt="CtrlEdu Logo" />
            <span className="text-xl tracking-tight"></span>
          </div>
          <ul className="hidden lg:flex ml-14 space-x-12">
            {navItems.map((item: NavItem, index: number) => (
              <li key={index}>
                <Link
                  to={item.href}
                  smooth={true}
                  duration={500}
                  offset={-120} // Adjust for sticky navbar height
                  className="cursor-pointer hover:text-orange-500 transition-colors"
                >
                  {item.label}
                </Link>
              </li>
            ))}
          </ul>
          <div className="hidden lg:flex justify-center space-x-6 items-center">
            <a
              onClick={() => navigate("/login")}
              className="py-2 px-3 border rounded-md hover:bg-neutral-200 dark:hover:bg-neutral-700 transition"
            >
              Sign In
            </a>
            <a
              onClick={() => navigate("/register")}
              className="bg-gradient-to-r from-orange-500 to-orange-800 py-2 px-3 rounded-md text-white"
            >
              Create an account
            </a>
          </div>
          <div className="lg:hidden flex">
            <button onClick={toggleNavbar} aria-label="Toggle navigation menu">
              {mobileDrawerOpen ? <X /> : <Menu />}
            </button>
          </div>
        </div>
        {mobileDrawerOpen && (
          <div className="fixed inset-0 z-20 bg-neutral-900 bg-opacity-90 p-12 flex flex-col justify-center items-center lg:hidden">
            <ul className="space-y-6">
              {navItems.map((item: NavItem, index: number) => (
                <li key={index} className="text-white text-lg">
                  <Link
                    to={item.href}
                    smooth={true}
                    duration={500}
                    offset={-80} // Adjust for sticky navbar height
                    className="cursor-pointer"
                    onClick={() => setMobileDrawerOpen(false)}
                  >
                    {item.label}
                  </Link>
                </li>
              ))}
            </ul>
            <div className="flex space-x-6 mt-8">
              <a
                href="#"
                className="py-2 px-3 border rounded-md text-white"
                onClick={() => setMobileDrawerOpen(false)}
              >
                Sign In
              </a>
              <a
                href="#"
                className="py-2 px-3 rounded-md bg-gradient-to-r from-orange-500 to-orange-800 text-white"
                onClick={() => setMobileDrawerOpen(false)}
              >
                Create an account
              </a>
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
