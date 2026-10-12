import React from 'react';
import { Scale, Github, Shield } from 'lucide-react';

export default function Navbar() {
  return (
    <nav className="bg-white shadow-lg border-b border-gray-200">
      <div className="container mx-auto px-6 py-4">
        <div className="flex justify-between items-center">
          <div className="flex items-center space-x-3">
            <Scale className="w-8 h-8 text-purple-600" />
            <span className="text-2xl font-bold gradient-bg bg-clip-text text-transparent">
              LegalValidator
            </span>
          </div>
          
          <div className="flex items-center space-x-6">
            <div className="flex items-center space-x-2 text-green-600">
              <Shield className="w-4 h-4" />
              <span className="text-sm font-semibold">Mock AI Mode</span>
            </div>
            <a href="#" className="text-gray-600 hover:text-purple-600 transition">
              <Github className="w-5 h-5" />
            </a>
          </div>
        </div>
      </div>
    </nav>
  );
}