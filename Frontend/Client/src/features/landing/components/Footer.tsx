import React from "react";
import {
  resourcesLinks,
  platformLinks,
  communityLinks,
} from "../../../constants";

type Link = {
  href: string;
  text: string;
};

const Footer: React.FC = () => {
  return (
    <footer className="mt-20 border-t py-10 border-neutral-700 bg-white dark:bg-neutral-900 text-black dark:text-white">
      <div className="grid grid-cols-2 lg:grid-cols-3 gap-4">
        <div>
          <h3 className="text-md font-semibold mb-4">Resources</h3>
          <ul className="space-y-2">
            {resourcesLinks.map((link: Link, index: number) => (
              <li key={index}>
                <a
                  href={link.href}
                  className="text-neutral-600 hover:text-black dark:text-neutral-300 dark:hover:text-white"
                >
                  {link.text}
                </a>
              </li>
            ))}
          </ul>
        </div>
        <div>
          <h3 className="text-md font-semibold mb-4">Platform</h3>
          <ul className="space-y-2">
            {platformLinks.map((link: Link, index: number) => (
              <li key={index}>
                <a
                  href={link.href}
                  className="text-neutral-600 hover:text-black dark:text-neutral-300 dark:hover:text-white"
                >
                  {link.text}
                </a>
              </li>
            ))}
          </ul>
        </div>
        <div>
          <h3 className="text-md font-semibold mb-4">Community</h3>
          <ul className="space-y-2">
            {communityLinks.map((link: Link, index: number) => (
              <li key={index}>
                <a
                  href={link.href}
                  className="text-neutral-600 hover:text-black dark:text-neutral-300 dark:hover:text-white"
                >
                  {link.text}
                </a>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
