import { NavLink } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Package, 
  ShoppingCart, 
  Users, 
  UserCircle,
  Truck,
  Tag,
  BarChart3
} from 'lucide-react';

const Sidebar = () => {
  const menuItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/' },
    { icon: Package, label: 'Stocks', path: '/stocks' },
    { icon: ShoppingCart, label: 'Caisses', path: '/caisses' },
    { icon: Users, label: 'Clients', path: '/clients' },
    { icon: UserCircle, label: 'Employés', path: '/employes' },
    { icon: Truck, label: 'Fournisseurs', path: '/fournisseurs' },
    { icon: Tag, label: 'Promotions', path: '/promotions' },
    { icon: BarChart3, label: 'Rapports', path: '/rapports' },
  ];

  return (
    <aside className="w-64 bg-white shadow-lg h-screen fixed left-0 top-0 overflow-y-auto">
      <div className="p-6">
        <h1 className="text-2xl font-bold text-primary-600">SuperMarket</h1>
        <p className="text-sm text-gray-500">Manager Pro</p>
      </div>
      
      <nav className="mt-6">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center px-6 py-3 text-gray-700 hover:bg-primary-50 hover:text-primary-600 transition-colors ${
                isActive ? 'bg-primary-50 text-primary-600 border-r-4 border-primary-600' : ''
              }`
            }
          >
            <item.icon className="w-5 h-5 mr-3" />
            <span className="font-medium">{item.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
};

export default Sidebar;

