import React from "react";
import styles from "./Header.module.scss";

const Header: React.FC = () => {
  return (
    <header className={styles.headerContainer}>
      <div className={styles.logo}>CtrlEdu</div>
      <nav className={styles.navLinks}>
        <a href="#features">Features</a>
        <a href="#about">About Us</a>
        <a href="#pricing">Pricing</a>
        <a href="#blog">Blog</a>
        <a href="#faq">FAQ</a>
      </nav>
      <a href="#get-free-trial" className={styles.callToAction}>
        Get free trial
      </a>
    </header>
  );
};

export default Header;
