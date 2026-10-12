import React from 'react';

export default function RiskMeter({ score }) {
  const getRiskColor = (score) => {
    if (score >= 75) return 'bg-red-600';
    if (score >= 50) return 'bg-orange-500';
    if (score >= 25) return 'bg-yellow-500';
    return 'bg-green-500';
  };
  
  const getRiskText = (score) => {
    if (score >= 75) return { text: 'CRITICAL RISK', color: 'text-red-600', emoji: '🔴' };
    if (score >= 50) return { text: 'HIGH RISK', color: 'text-orange-600', emoji: '🟠' };
    if (score >= 25) return { text: 'MEDIUM RISK', color: 'text-yellow-600', emoji: '🟡' };
    return { text: 'LOW RISK', color: 'text-green-600', emoji: '🟢' };
  };
  
  const risk = getRiskText(score);
  
  return (
    <div className="bg-white rounded-2xl shadow-lg p-6">
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-xl font-bold text-gray-800">Risk Assessment</h3>
        <span className={`text-2xl ${risk.color}`}>
          {risk.emoji} {risk.text}
        </span>
      </div>
      
      <div className="relative h-4 bg-gray-200 rounded-full overflow-hidden mb-3">
        <div 
          className={`absolute left-0 top-0 h-full transition-all duration-1000 ${getRiskColor(score)}`}
          style={{ width: `${score}%` }}
        >
          <div className="absolute inset-0 bg-white opacity-20 animate-pulse"></div>
        </div>
      </div>
      
      <div className="flex justify-between text-sm text-gray-600">
        <span>🟢 0</span>
        <span>🟡 25</span>
        <span>🟠 50</span>
        <span>🔴 75</span>
        <span>💀 100</span>
      </div>
    </div>
  );
}