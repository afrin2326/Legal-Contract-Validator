import React from 'react';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, Legend } from 'recharts';

const RiskChart = ({ issues }) => {
    // ১. ইস্যুগুলো থেকে ডেটা ফরম্যাট করা
    const dataCount = issues.reduce((acc, issue) => {
        const severity = issue.severity || 'OTHER';
        acc[severity] = (acc[severity] || 0) + 1;
        return acc;
    }, {});

    const chartData = Object.keys(dataCount).map(key => ({
        name: key,
        value: dataCount[key]
    }));

    // ২. কালার সেট করা
    const COLORS = {
        CRITICAL: '#ef4444', // Red
        HIGH: '#f97316',     // Orange
        MEDIUM: '#eab308',   // Yellow
        LOW: '#3b82f6'       // Blue
    };

    return (
        <div style={{ width: '100%', height: 300, backgroundColor: '#f8fafc', padding: '20px', borderRadius: '12px' }}>
            <h3 style={{ textAlign: 'center', marginBottom: '10px' }}>রিস্ক অ্যানালাইসিস চার্ট</h3>
            <ResponsiveContainer>
                <PieChart>
                    <Pie
                        data={chartData}
                        cx="50%"
                        cy="50%"
                        innerRadius={60}
                        outerRadius={80}
                        paddingAngle={5}
                        dataKey="value"
                    >
                        {chartData.map((entry, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[entry.name] || '#8884d8'} />
                        ))}
                    </Pie>
                    <Tooltip />
                    <Legend />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
};

export default RiskChart;