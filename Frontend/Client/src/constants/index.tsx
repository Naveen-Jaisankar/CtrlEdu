import {
  BotMessageSquare,
  Fingerprint,
  ShieldHalf,
  PlugZap,
  GlobeLock,
} from "lucide-react";

import user1 from "../assets/profile-pictures/user1.jpg";
import user2 from "../assets/profile-pictures/user2.jpg";
import user3 from "../assets/profile-pictures/user3.jpg";
import user4 from "../assets/profile-pictures/user4.jpg";
import user5 from "../assets/profile-pictures/user5.jpg";
import user6 from "../assets/profile-pictures/user6.jpg";

export const navItems = [
  { label: "Features", href: "#" },
  { label: "Why CtrlEdu", href: "#" },
  { label: "Pricing", href: "#" },
  { label: "Ctrl Suite", href: "#" },
  { label: "Blog", href: "#" },
  { label: "FAQ", href: "#" },
];

export const testimonials = [
  {
    user: "Sarah Thompson",
    company: "Greenwood High School",
    image: user1,
    text: "CtrlEdu has completely transformed how we manage our school operations. From admissions to attendance, everything is now streamlined and efficient. The support team is incredibly responsive, ensuring we get the most out of every feature.",
  },
  {
    user: "Mark Alvarez",
    company: "Riverdale Academy",
    image: user2,
    text: "Our teachers love CtrlEdu's communication and academic management tools. It has simplified lesson planning, student grading, and parent communication. The platform is user-friendly, making it easy for everyone to adopt.",
  },
  {
    user: "Linda Parker",
    company: "Horizon University",
    image: user3,
    text: "The AI-driven features in CtrlEdu help us manage a large student body effectively. It feels like the platform is tailored to our needs, continuously learning and optimizing as we grow. Highly recommended for large institutions!",
  },
  {
    user: "James Carter",
    company: "Innovative Learning Center",
    image: user4,
    text: "CtrlEdu's comprehensive campus management tools have reduced our reliance on multiple systems. Everything is now in one place, from student data to HR management. It's been a game-changer for our institution.",
  },
  {
    user: "Emily Sanders",
    company: "Silver Oak School",
    image: user5,
    text: "The simplicity of CtrlEdu's interface is a huge plus. Our staff and teachers were able to start using it with minimal training, and parents appreciate the easy access to student progress and communication tools.",
  },
  {
    user: "Michael Johnson",
    company: "Global Prep Academy",
    image: user6,
    text: "CtrlEdu’s support team is always there for us. The implementation process was smooth, and their dedication to ensuring we succeed with the platform has been outstanding. It’s a must-have for any educational institution.",
  },
];

export const features = [
  {
    icon: <PlugZap />,
    text: "Admission",
    description:
      "Our admissions feature simplifies the application and enrollment process. With online forms, digital document management, and automated notifications, schools can easily manage admissions while reducing paperwork. It's all about making the experience smooth and efficient for everyone involved!",
  },
  {
    icon: <Fingerprint />,
    text: "Academics",
    description:
      "Our academics feature centralizes and simplifies management. Easily organize curricula, track dairy management, manage timetables. Everything you need for a smooth academic process is in one place.",
  },
  {
    icon: <BotMessageSquare />,
    text: "Communication",
    description:
      "Enhance interactions with our communication feature, which centralizes messaging between teachers, parents, and students. Easily share updates, announcements, and important information, ensuring everyone stays informed and connected.",
  },
  {
    icon: <GlobeLock />,
    text: "Exams & Grades",
    description:
      "Effortlessly manage exams and grades with our integrated system. Create and schedule exams, record results, and track student performance. Generate detailed grade reports and ensure accurate, up-to-date records for better academic oversight.",
  },
  {
    icon: <ShieldHalf />,
    text: "HR Management System",
    description:
      "Streamline human resources with our HRMS feature. Manage staff records, handle payroll, track leave requests, and oversee performance evaluations. Simplify HR tasks and keep all employee information organized and accessible.",
  },
  {
    icon: <Fingerprint />,
    text: "Attendance",
    description:
      "Effortlessly track student attendance with our system. Record and monitor daily attendance, generate reports, and manage absences all in one place.",
  },
];

export const checklistItems = [
  {
    title: "AI-Driven Institutional Ecosystem",
    description:
      "Our platform goes beyond basic automation. With an AI-driven ecosystem, We continuously learns and optimizes your Campus operations.",
  },
  {
    title: "Unified Solution for All Campus Needs",
    description:
      "We is not just software; it’s a comprehensive campus management solution. From admissions and attendance to fee collection, timetable management, and communication, everything your school needs is under one roof. No more juggling multiple platforms—We handles it all with ease.",
  },
  {
    title:
      "Friendly Interface,\
Quick to Learn",
    description:
      "At CtrlEdu, we prioritize simplicity and usability. Our intuitive interface is designed for quick adoption, so your staff, teachers, and parents can easily navigate the system without needing extensive training. Whether it’s managing hostel accommodations or organizing school events, CtrlEdu is built to be used by everyone, with minimal friction.",
  },
  {
    title: "Dedicated Support, Every Step of the Way",
    description:
      "We know that implementation is just the first step. CtrlEdu provides ongoing support through dedicated Customer Success Managers, regular training sessions, and a comprehensive self-learn portal. Whether it’s resolving a quick issue or customizing a feature to fit your school’s needs, we’re here to ensure your success.",
  },
  {
    title: "Full Utilization of Purchased Modules",
    description:
      "Unlike other platforms where schools might use only a fraction of the available features, CtrlEdu ensures full utilization of every module you purchase. We design our system to be adaptable and relevant to your specific needs, maximizing the return on your investment.",
  },
];

export const pricingOptions = [
  {
    title: "Foundation",
    price: "$0",
    features: [
      "Admissions",
      "Student Analytics",
      "Academics Planner",
      "Exams and Results",
      "Communication",
      "Human Resource (HR)",
    ],
  },
  {
    title: "Growth",
    price: "$10",
    features: [
      "All Foundation Modules +",
      "Admission Outreach & CRM",
      "Payroll Management",
      "Campus Financial Performance Report",
      "Workflow Automation",
    ],
  },
  {
    title: "Enterprise",
    price: "$200",
    features: [
      "All Modules from Growth",
      "Infra Management",
      "Branded Apps (Parent App)",
      "Feedback & Concern Handling Management",
    ],
  },
];

export const resourcesLinks = [
  { href: "#", text: "Getting Started" },
  { href: "#", text: "Documentation" },
  { href: "#", text: "Tutorials" },
  { href: "#", text: "API Reference" },
  { href: "#", text: "Community Forums" },
];

export const platformLinks = [
  { href: "#", text: "Features" },
  { href: "#", text: "Supported Devices" },
  { href: "#", text: "System Requirements" },
  { href: "#", text: "Downloads" },
  { href: "#", text: "Release Notes" },
];

export const communityLinks = [
  { href: "#", text: "Events" },
  { href: "#", text: "Meetups" },
  { href: "#", text: "Conferences" },
  { href: "#", text: "Hackathons" },
  { href: "#", text: "Jobs" },
];
