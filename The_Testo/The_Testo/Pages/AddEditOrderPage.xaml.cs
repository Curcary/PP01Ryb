using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using The_Testo.Database;
using The_Testo.Printers;

namespace The_Testo.Pages
{
    /// <summary>
    /// Логика взаимодействия для AddEditOrderPage.xaml
    /// </summary>
    public partial class AddEditOrderPage : Page
    {
        private Order _order;


        public AddEditOrderPage(Order order = null)
        {
            InitializeComponent();
            if (order != null)
                this._order = order;
            else
            {
                this._order = DB.entities.Order.Create();
                this._order.OrderDate = DateTime.Now;
                this._order.User = GlobalInfo.CurrentUser;
            }
        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {
            if (GlobalInfo.SelectedDish != null)
            {
                var od = DB.entities.Ordered_dishes.Create();
                od.Dish = GlobalInfo.SelectedDish;
                od.DishAmount = 1;
                od.Order = _order; 
                od.OrderDate = DateTime.Now;
                _order.Ordered_dishes.Add( od );
            }
            DataContext = _order;
            OrderedDishesView.ItemsSource =  _order.Ordered_dishes.ToList();
        }

        private void Page_Unloaded(object sender, RoutedEventArgs e)
        {
            GlobalInfo.SelectedDish = null;
        }

        private void AddDish(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new DishesPage(true));
        }

        private void DeleteDish(object sender, RoutedEventArgs e)
        {

            if (OrderedDishesView.SelectedItem is Ordered_dishes od)
            {
                if (MessageBox.Show("Вы действительно хотите" +
                    " удалить данное блюдо?", "", MessageBoxButton.YesNo) == MessageBoxResult.Yes)
                {
                    _order.Ordered_dishes.Remove(od);
                    MessageBox.Show("Блюдо успешно удалено");
                    OrderedDishesView.ItemsSource = _order.Ordered_dishes.ToList() ;
                }
            }
            else MessageBox.Show("Пожалуйста, укажите удаляемое блюдо");
        }

        private void Accept(object sender, RoutedEventArgs e)
        {
            if (_order.OrderID == 0)
            {
                string instruction = "insert into [Order] (OrderDate, OrderTableNum, UserID) values ('" +
                    _order.OrderDate + "', " + _order.OrderTableNum + ", " + _order.User.UserID + ")";
                DB.entities.Database.ExecuteSqlCommand(instruction, "");
                DB.entities = new The_TestoEntities();
                Order neworder = DB.entities.Order.ToList().Last();
                foreach (var item in _order.Ordered_dishes)
                {
                    instruction = "insert into [Ordered_dishes] values ("
                        +item.Dish.DishID+", "+item.DishAmount+", "+neworder.OrderID+", '"+neworder.OrderDate+"')";
                    DB.entities.Database.ExecuteSqlCommand(instruction, "");
                }
            }
            else
                DB.entities.SaveChanges();
            NavigationService.Navigate(new CurrentOrdersPage());
        }

        private void Decline(object sender, RoutedEventArgs e)
        {
            DB.entities.Order.Load();
            NavigationService.Navigate(new CurrentOrdersPage());
        }
    }
}
